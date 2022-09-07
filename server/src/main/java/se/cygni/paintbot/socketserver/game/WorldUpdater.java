package se.cygni.paintbot.socketserver.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import se.cygni.game.Coordinate;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.enums.Action;
import se.cygni.game.exception.OutOfBoundsException;
import se.cygni.game.exception.TransformationException;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.game.worldobject.Character;
import se.cygni.game.worldobject.*;
import se.cygni.paintbot.api.model.PointReason;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorldUpdater {
    private XORShiftRandom random = new XORShiftRandom();
    private final PlayerManager playerManager;
    private ThreadLocal<WorldState> startingWorldState = new ThreadLocal<>();

//    public WorldUpdater(PlayerManager playerManager) {
//        this.playerManager = playerManager;
//    }

    public WorldState update(
            Map<String, Action> actions,
            GameFeatures gameFeatures,
            WorldState ws,
            long worldTick) throws TransformationException {

        startingWorldState.set(ws);

        WorldState nextWorld = new WorldState(ws);

        Map<String, Integer> originalPositions = actions.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        e -> ws.getCharacterById(e.getKey()).getPosition()
                )
        );
        ConcurrentHashMap<Integer, List<String>> updatedPositions = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Integer> stunsCaused = new ConcurrentHashMap<>();
        var stunnedPlayers = new HashSet<String>();

        actions.entrySet().forEach(entry -> {
            String player = entry.getKey();
            Action action = entry.getValue();
            int nextPosition = originalPositions.get(player);
            if (action.isMovement() && canPerform(ws, player)) {
                nextPosition = getNextPosition(entry, ws);
                if (ws.isTileContentOfType(nextPosition, Obstacle.class) || isCollidingWithNeighbour(ws, entry, actions)) {
                    stunnedPlayers.add(player);
                    nextPosition = originalPositions.get(player);
                }
            }

            updatePosition(updatedPositions, player, nextPosition);
        });

        // Move all colliding player to original position
        while (updatedPositions.entrySet().stream().anyMatch(e -> e.getValue().size() > 1)) {
            var collidingPlayers = updatedPositions.entrySet()
                    .stream()
                    .filter(e -> e.getValue().size() > 1)
                    .flatMap(e -> e.getValue().stream().map(p -> Pair.of(e.getKey(), p)))
                    .collect(Collectors.toList());
            collidingPlayers.forEach(positionPlayerPair -> {
                String player = positionPlayerPair.getValue();
                Integer collidingPosition = positionPlayerPair.getKey();
                stunnedPlayers.add(player);
                List<String> playersAtCollidingPosition = updatedPositions.get(collidingPosition);
                List<String> withoutPlayer = playersAtCollidingPosition.stream().filter(p -> !p.equals(player))
                        .collect(toList());
                updatedPositions.put(collidingPosition, withoutPlayer);
                updatePosition(updatedPositions, player, originalPositions.get(player));
            });
        }

        updatedPositions.forEach((p, l) -> {
            if (l.isEmpty()) {
                updatedPositions.remove(p);
            }
        });

        Tile[] tiles = nextWorld.getTiles();

        for (int position = 0; position < tiles.length; position++) {
            if (updatedPositions.containsKey(position)) {
                String playerId = updatedPositions.get(position).get(0);
                var hasPickedUpPowerUp = ws.getTile(position).getContent() instanceof PowerUp;
                updateCharacterState(tiles, position, nextWorld.getCharacterById(playerId), hasPickedUpPowerUp);
            } else if (originalPositions.containsValue(position)) {
                tiles[position] = new Tile(new Empty(), ws.getTile(position).getOwnerID());
            }
        }

        WorldState positionUpdatedWorld = ws.withTiles(tiles);

        Map<Integer, List<String>> positionsExploded = new HashMap<>();
        actions.entrySet().stream()
                .filter(e ->
                        e.getValue().equals(Action.EXPLODE) &&
                                ws.getCharacterById(e.getKey()).isCarryingPowerUp() &&
                                canPerform(ws, e.getKey())
                )
                .forEach(e -> {
                    String playerId = e.getKey();
                    var player = positionUpdatedWorld.getCharacterById(playerId);
                    player.setCarryingPowerUp(false);
                    int position = player.getPosition();
                    Coordinate myCoordinate = positionUpdatedWorld.translatePosition(position);

                    for (int dx = myCoordinate.getX() - gameFeatures.getExplosionRange();
                         dx <= myCoordinate.getX() + gameFeatures.getExplosionRange();
                         dx++
                    ) {
                        for (int dy = myCoordinate.getY() - gameFeatures.getExplosionRange();
                             dy <= myCoordinate.getY() + gameFeatures.getExplosionRange();
                             dy++) {

                            Coordinate coordinate = new Coordinate(dx, dy);
                            if (isWithinBounds(ws, coordinate) &&
                                    manhattanDistance(myCoordinate, coordinate) <= gameFeatures.getExplosionRange() &&
                                    !coordinate.equals(myCoordinate)
                            ) {
                                int currPos = positionUpdatedWorld.translateCoordinate(coordinate);
                                positionUpdatedWorld.getTile(currPos);

                                List<String> playersExplodingPosition = positionsExploded
                                        .getOrDefault(currPos, new LinkedList<>());
                                playersExplodingPosition.add(playerId);
                                positionsExploded.put(currPos, playersExplodingPosition);
                            }
                        }

                    }
                });

        nextWorld.setExplosions(positionsExploded);

        positionsExploded.forEach((position, players) -> {
            WorldObject content = positionUpdatedWorld.getTile(position).getContent();
            // Randomly select one player to successfully explode on this tile
            String playerId = players.get(random.nextInt(players.size()));
            if (content instanceof Empty || content instanceof PowerUp) {
                tiles[position] = new Tile(content, playerId);
            } else if (content instanceof Character) {
                stunnedPlayers.add(positionUpdatedWorld.getCharacterAtPosition(position).getPlayerId());
                increaseStunsCaused(stunsCaused, playerId);
            }
        });

        // Set colliding players to stunned
        stunnedPlayers.forEach(p -> nextWorld.getCharacterById(p).setIsStunnedForTicks(gameFeatures.getRandomNoOfTicksStunned()));

        WorldState explosionsHappenedWorld = ws.withTiles(tiles);

        playerManager.getLivePlayers().forEach(p -> {
            if (!gameFeatures.isPointsPerTick()) {
                int oldOwnedTiles = ws.listPositionWithOwner(p.getPlayerId()).length;
                int ownedTiles = explosionsHappenedWorld.listPositionWithOwner(p.getPlayerId()).length;
                p.addPoints(PointReason.OWNED_TILES, (ownedTiles - oldOwnedTiles) * gameFeatures.getPointsPerTileOwned());
            } else {
                int ownedTiles = explosionsHappenedWorld.listPositionWithOwner(p.getPlayerId()).length;
                p.addPoints(PointReason.OWNED_TILES, ownedTiles * gameFeatures.getPointsPerTileOwned());
            }
            p.addPoints(PointReason.CAUSED_STUN, stunsCaused.getOrDefault(p.getPlayerId(), 0) * gameFeatures.getPointsPerCausedStun());

            explosionsHappenedWorld.getCharacterById(p.getPlayerId()).setPoints(p.getTotalPoints());
        });

        return ws.withTiles(tiles)
            .withCollisions(nextWorld.getCollisions())
            .withExplosions(nextWorld.getExplosions());
    }

    private void increaseStunsCaused(ConcurrentHashMap<String, Integer> stunsCaused, String playerId) {
        Integer stuns = stunsCaused.getOrDefault(playerId, 0);
        stunsCaused.put(playerId, stuns + 1);
    }

    private void updatePosition(ConcurrentHashMap<Integer, List<String>> updatedPositions, String player, Integer newPosition) {
        List<String> playersAtPosition = updatedPositions.getOrDefault(newPosition, new LinkedList<>());
        playersAtPosition.add(player);
        updatedPositions.put(newPosition, playersAtPosition);
    }

    private boolean isCollidingWithNeighbour(WorldState ws, Map.Entry<String, Action> entry, Map<String, Action> actions) {
        int nextPosition = getNextPosition(entry, ws);
        if (ws.isTileContentOfType(nextPosition, CharacterImpl.class)) {
            String otherPlayer = ws.getCharacterAtPosition(nextPosition).getPlayerId();
            return actions.get(otherPlayer).isOppositeMovement(entry.getValue());
        }

        return false;
    }

    private int manhattanDistance(Coordinate myCoordinate, Coordinate coordinate) {
        return Math.abs(myCoordinate.getX() - coordinate.getX()) + Math.abs(myCoordinate.getY() - coordinate.getY());
    }

    private boolean isWithinBounds(WorldState worldState, Coordinate coordinate) {
        return coordinate.getX() >= 0 && coordinate.getX() < worldState.getWidth()
                && coordinate.getY() >= 0 && coordinate.getY() < worldState.getHeight();
    }

    private boolean canPerform(WorldState ws, String playerId) {
        return ws.getCharacterById(playerId).getIsStunnedForTicks() == 0;
    }

    private void updateCharacterState(Tile[] tiles, int targetPosition, Character character, boolean hasPickedUpPowerUp) {
        character.setPosition(targetPosition);
        if (hasPickedUpPowerUp) {
            character.setCarryingPowerUp(true);
        }
        tiles[targetPosition] = new Tile(character, character.getPlayerId());
    }

    private int getNextPosition(Map.Entry<String, Action> updateEntry, WorldState ws) {
        var currentPos = ws.getCharacterById(updateEntry.getKey()).getPosition();
        try {
            return ws.getPositionForAdjacent(currentPos, updateEntry.getValue());
        } catch (OutOfBoundsException e) {
            //Don't run out of the map, you'll get nowhere.
            return currentPos;
        }
    }
}
