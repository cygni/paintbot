package se.cygni.paintbot.socketserver.player.bot;

//import com.google.common.eventbus.EventBus;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.paintbot.api.event.MapUpdateEvent;
import se.cygni.paintbot.api.model.CharacterAction;
import se.cygni.paintbot.api.model.Map;
import se.cygni.paintbot.client.MapUtilityImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RandomBot extends BotPlayer {

    private CharacterAction myLastDirection;
    private XORShiftRandom random = new XORShiftRandom();

//    public RandomBot(String playerId, EventBus incomingEventbus) {
    public RandomBot(String playerId) {
//        super(playerId, incomingEventbus);
        super(playerId);
    }

    @Override
    public void onWorldUpdate(MapUpdateEvent mapUpdateEvent) {
        CompletableFuture.runAsync(() -> postNextMove(mapUpdateEvent));
    }

    private void postNextMove(MapUpdateEvent mapUpdateEvent) {
        Map map = mapUpdateEvent.getMap();
        MapUtilityImpl mapUtil = new MapUtilityImpl(map, playerId);

        CharacterAction rndDirection = getRandomDirection();
        List<CharacterAction> validDirections = getValidDirections(mapUtil);
        if (validDirections.size() > 0) {
            rndDirection = getRandomDirection(validDirections);
        }
        myLastDirection = rndDirection;

        registerMove(mapUpdateEvent, rndDirection);
    }

    private List<CharacterAction> getValidDirections(MapUtilityImpl mapUtil) {

        List<CharacterAction> validDirections = new ArrayList<>();

        for (CharacterAction direction : CharacterAction.values()) {
            if (mapUtil.canIMoveInDirection(direction))
                validDirections.add(direction);
        }

        return validDirections;
    }

    private CharacterAction getRandomDirection(List<CharacterAction> directions) {

        // Let's prefer the last direction if it is available
        if (directions.contains(myLastDirection)) {
            if (random.nextDouble() < 0.5) {
                return myLastDirection;
            }
        }

        int max = directions.size()-1;
        if (max == 0)
            return directions.get(0);

        return directions.get(random.nextInt(max));
    }

    private CharacterAction getRandomDirection() {
        int max = CharacterAction.values().length-1;

        return CharacterAction.values()[random.nextInt(max)];
    }
}