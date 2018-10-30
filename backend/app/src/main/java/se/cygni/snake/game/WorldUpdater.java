package se.cygni.snake.game;

import com.google.common.eventbus.EventBus;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.game.Coordinate;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.enums.Action;
import se.cygni.game.exception.OutOfBoundsException;
import se.cygni.game.exception.TransformationException;
import se.cygni.game.transformation.PerformCharacterAction;
import se.cygni.game.worldobject.Character;
import se.cygni.game.worldobject.CharacterImpl;
import se.cygni.game.worldobject.Empty;
import se.cygni.snake.api.event.CharacterStunnedEvent;
import se.cygni.snake.api.model.StunReason;
import se.cygni.snake.apiconversion.GameMessageConverter;
import se.cygni.snake.client.MapUtil;
import se.cygni.snake.event.InternalGameEvent;
import se.cygni.snake.player.IPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class WorldUpdater {

    private static final Logger log = LoggerFactory.getLogger(WorldUpdater.class);

    private final GameFeatures gameFeatures;
    private final PlayerManager playerManager;
    private final String gameId;
    private final EventBus globalEventBus;
    private ThreadLocal<WorldState> startingWorldState = new ThreadLocal<>();

    public WorldUpdater(GameFeatures gameFeatures, PlayerManager playerManager, String gameId, EventBus globalEventBus) {
        this.gameFeatures = gameFeatures;
        this.playerManager = playerManager;
        this.gameId = gameId;
        this.globalEventBus = globalEventBus;
    }

    public WorldState update(
            Map<String, Action> actions,
            GameFeatures gameFeatures,
            WorldState ws,
            long worldTick) throws TransformationException {

        startingWorldState.set(ws);

        WorldState nextWorld = new WorldState(ws);

        Map<Integer, List<String>> playersPositions = new HashMap<>();

        Map<String, Integer> originalPositions = actions.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        e -> ws.getCharacterById(e.getKey()).getPosition()
                )
        );

        ConcurrentHashMap<Integer, List<String>> updatedPositions = new ConcurrentHashMap<>(actions.entrySet().stream()
                .filter(e -> e.getValue().isMovement())
                .map(e -> getNextPosition(e, ws))
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList()))));


        while(updatedPositions.entrySet().stream().anyMatch(e -> e.getValue().size() > 1)){
            var collidingEntries = updatedPositions.entrySet()
                    .stream()
                    .filter(e -> e.getValue().size() > 1)
                    .collect(toList());
            for (var entry : collidingEntries) {
                List<String> colliders = updatedPositions.get(entry.getKey());
                for(var player : entry.getValue()) {
                    Integer originalPosition = originalPositions.get(player);
                    List<String> playersAtPos = updatedPositions.getOrDefault(originalPosition, new LinkedList<>());
                    playersAtPos.add(player);
                    colliders.remove(player);
                    updatedPositions.put(originalPosition, playersAtPos);
                }
            }
        }



        for(var e : updatedPositions.entrySet()) {
            for(var p : e.getValue()) {
                updateCharacterState(nextWorld.getTiles(), e.getKey(), ws.getCharacterById(p), false);
            }
        }


        // TODO Handle stuns
        // TODO Handle explosions

        return nextWorld;

    }


    private void updateCharacterState(Tile[] tiles, int targetPosition, Character character, boolean hasPickedUpBomb) {
        Tile currentTile = tiles[character.getPosition()];
        tiles[character.getPosition()] = new Tile(new Empty(), currentTile.getOwnerID());
        tiles[targetPosition] = new Tile(character, character.getPlayerId());
        character.setPosition(targetPosition);
    }

    private Pair<Integer, String> getNextPosition(Map.Entry<String, Action> updateEntry, WorldState ws) {
        var currentPos = ws.getCharacterById(updateEntry.getKey()).getPosition();
        try {
            return Pair.of(ws.getPositionForAdjacent(currentPos, updateEntry.getValue()), updateEntry.getKey());
        } catch (OutOfBoundsException e) {
            //Don't run out of the map, you'll get nowhere.
            return Pair.of(currentPos, updateEntry.getKey());
        }
    }


    private void syncPoints(WorldState ws) {
        playerManager.toSet().forEach(player -> {
            if (player.isAlive())
                ws.getCharacterById(player.getPlayerId()).setPoints(player.getTotalPoints());
        });
    }

    private void notifyCharacterStunned(
            CharacterImpl head,
            StunReason stunReason,
            Coordinate coordinate,
            long worldTick) {

        IPlayer stunnedCharacter = playerManager.getPlayer(head.getPlayerId());
        log.info("Stun occurred by: {}. GameId: {}, Player: {}, with id: {}, stunned at: {}",
                stunReason,
                gameId,
                stunnedCharacter.getName(),
                stunnedCharacter.getPlayerId(),
                coordinate);

        stunnedCharacter.stunned(worldTick);

        CharacterStunnedEvent characterStunnedEvent = GameMessageConverter.onPlayerStunned(
                stunReason,
                gameFeatures.getNoOfTicksStunned(),
                head.getPlayerId(),
                coordinate.getX(), coordinate.getY(),
                gameId, worldTick);

        playerManager.toSet().stream().forEach(player -> {
            player.onCharacterStunned(characterStunnedEvent);
        });

        InternalGameEvent gevent = new InternalGameEvent(
                System.currentTimeMillis(),
                characterStunnedEvent);
        globalEventBus.post(gevent);
    }
}
