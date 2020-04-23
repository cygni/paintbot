package se.cygni.paintbot.player.bot;

import com.google.common.eventbus.EventBus;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.paintbot.api.event.MapUpdateEvent;
import se.cygni.paintbot.api.model.CharacterAction;
import se.cygni.paintbot.client.MapCoordinate;
import se.cygni.paintbot.client.MapUtilityImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PoweredBot extends BotPlayer {
    private CharacterAction lastDirection = CharacterAction.STAY;
    private XORShiftRandom random = new XORShiftRandom();

    public PoweredBot(String playerId, EventBus incomingEventbus) {
        super(playerId, incomingEventbus);
    }

    @Override
    public void onWorldUpdate(MapUpdateEvent mapUpdateEvent) {
        CompletableFuture.runAsync(() -> postNextMove(mapUpdateEvent));
    }

    private void postNextMove(MapUpdateEvent mapUpdateEvent) {
        MapUtilityImpl mapUtil = new MapUtilityImpl(mapUpdateEvent.getMap(), getPlayerId());

        if(mapUpdateEvent.getGameTick() % 10 == 0) {
            registerMove(mapUpdateEvent, CharacterAction.EXPLODE);
            return;
        }

        MapCoordinate closestPowerUp = BotUtils.findClosestPowerUp(mapUtil);
        if(closestPowerUp == null) {
            registerMove(mapUpdateEvent, lastDirection);
            return;
        }

        MapCoordinate myPosition = mapUtil.getMyCoordinate();
        List<CharacterAction> possibleActions = new ArrayList<>();
        if(closestPowerUp.x < myPosition.x) {
            possibleActions.add(CharacterAction.LEFT);
        } else if(closestPowerUp.x > myPosition.x) {
            possibleActions.add(CharacterAction.RIGHT);
        }

        if(closestPowerUp.y < myPosition.y) {
            possibleActions.add(CharacterAction.UP);
        } else if(closestPowerUp.y > myPosition.y) {
            possibleActions.add(CharacterAction.DOWN);
        }

        CharacterAction chosenDirection = lastDirection;
        List<CharacterAction> validActions = possibleActions.stream().filter(mapUtil::canIMoveInDirection)
                .collect(Collectors.toList());

        // Choose a random direction
        if (!validActions.isEmpty()) {
            chosenDirection = validActions.get(random.nextInt(validActions.size()));
        }

        // Register action here!
        registerMove(mapUpdateEvent, chosenDirection);
        lastDirection = chosenDirection;
    }
}
