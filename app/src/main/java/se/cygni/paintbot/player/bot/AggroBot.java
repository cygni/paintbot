package se.cygni.paintbot.player.bot;

import com.google.common.eventbus.EventBus;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.paintbot.api.event.GameStartingEvent;
import se.cygni.paintbot.api.event.MapUpdateEvent;
import se.cygni.paintbot.api.model.CharacterAction;
import se.cygni.paintbot.api.model.CharacterInfo;
import se.cygni.paintbot.client.MapCoordinate;
import se.cygni.paintbot.client.MapUtilityImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static se.cygni.paintbot.api.model.CharacterAction.EXPLODE;
import static se.cygni.paintbot.api.model.CharacterAction.STAY;

public class AggroBot extends BotPlayer {
    private XORShiftRandom random = new XORShiftRandom();
    private int explosionRange;

    public AggroBot(String playerId, EventBus incomingEventbus) {
        super(playerId, incomingEventbus);
    }

    @Override
    public void onGameStart(GameStartingEvent gameStartingEvent) {
        explosionRange = gameStartingEvent.getGameSettings().getExplosionRange();
    }

    @Override
    public void onWorldUpdate(MapUpdateEvent mapUpdateEvent) {
        CompletableFuture.runAsync(() -> postNextMove(mapUpdateEvent));
    }

    private void postNextMove(MapUpdateEvent mapUpdateEvent) {
        MapUtilityImpl mapUtil = new MapUtilityImpl(mapUpdateEvent.getMap(), getPlayerId());
        boolean shouldExplode = shouldExplode(mapUtil, mapUpdateEvent);
        boolean isCarryingBomb = mapUtil.getMyCharacterInfo().isCarryingPowerUp();
        if (isCarryingBomb && shouldExplode) {
            registerMove(mapUpdateEvent, EXPLODE);
            return;
        }

        if (isCarryingBomb) {
            MapCoordinate closestEnemy = findClosestEnemy(mapUtil, mapUpdateEvent);
            CharacterAction chosenDirection = getDirection(mapUtil, closestEnemy);
            registerMove(mapUpdateEvent, chosenDirection);
            return;
        }

        MapCoordinate closestPowerUp = BotUtils.findClosestPowerUp(mapUtil);
        if (closestPowerUp == null) {
            registerMove(mapUpdateEvent, STAY);
            return;
        }
        CharacterAction chosenDirection = getDirection(mapUtil, closestPowerUp);
        registerMove(mapUpdateEvent, chosenDirection);
    }

    private MapCoordinate findClosestEnemy(MapUtilityImpl mapUtil, MapUpdateEvent mapUpdateEvent) {
        MapCoordinate closest = null;
        int minDistance = Integer.MAX_VALUE;
        for (CharacterInfo player : mapUpdateEvent.getMap().getCharacterInfos()) {
            if (!player.getId().equals(playerId)) {
                int distance = mapUtil.getMyCoordinate()
                        .getManhattanDistanceTo(mapUtil.convertPositionToCoordinate(player.getPosition()));
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = mapUtil.convertPositionToCoordinate(player.getPosition());
                }
            }
        }

        return closest;
    }

    private boolean shouldExplode(MapUtilityImpl mapUtil, MapUpdateEvent mapUpdateEvent) {
        MapCoordinate myPosition = mapUtil.getMyCoordinate();
        CharacterInfo[] players = mapUpdateEvent.getMap().getCharacterInfos();

        int minDist = Integer.MAX_VALUE;
        for (CharacterInfo player : players) {
            if (!player.getId().equals(playerId)) {
                int dist = myPosition.getManhattanDistanceTo(mapUtil.convertPositionToCoordinate(player.getPosition()));
                minDist = Math.min(dist, minDist);
            }
        }

        return minDist <= explosionRange;
    }

    private CharacterAction getDirection(MapUtilityImpl mapUtil, MapCoordinate closestPowerUp) {
        MapCoordinate myPosition = mapUtil.getMyCoordinate();
        List<CharacterAction> possibleActions = new ArrayList<>();
        if (closestPowerUp.x < myPosition.x) {
            possibleActions.add(CharacterAction.LEFT);
        } else if (closestPowerUp.x > myPosition.x) {
            possibleActions.add(CharacterAction.RIGHT);
        }

        if (closestPowerUp.y < myPosition.y) {
            possibleActions.add(CharacterAction.UP);
        } else if (closestPowerUp.y > myPosition.y) {
            possibleActions.add(CharacterAction.DOWN);
        }

        CharacterAction chosenDirection = STAY;
        List<CharacterAction> validActions = possibleActions.stream().filter(mapUtil::canIMoveInDirection)
                .collect(Collectors.toList());

        if (!validActions.isEmpty()) {
            chosenDirection = validActions.get(random.nextInt(validActions.size()));
        }
        return chosenDirection;
    }
}
