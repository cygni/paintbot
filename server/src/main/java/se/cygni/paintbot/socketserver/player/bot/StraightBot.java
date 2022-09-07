package se.cygni.paintbot.socketserver.player.bot;

//import com.google.common.eventbus.EventBus;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.paintbot.api.event.MapUpdateEvent;
import se.cygni.paintbot.api.model.CharacterAction;
import se.cygni.paintbot.client.MapUtilityImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class StraightBot extends BotPlayer {

    CharacterAction lastDirection;
    private XORShiftRandom random = new XORShiftRandom();

//    public StraightBot(String playerId, EventBus incomingEventbus) {
    public StraightBot(String playerId) {
//        super(playerId, incomingEventbus);
        super(playerId);
        lastDirection = getRandomDirection();
    }

    @Override
    public void onWorldUpdate(MapUpdateEvent mapUpdateEvent) {
        CompletableFuture.runAsync(() -> postNextMove(mapUpdateEvent));
    }


    private void postNextMove(MapUpdateEvent mapUpdateEvent) {
        MapUtilityImpl mapUtil = new MapUtilityImpl(mapUpdateEvent.getMap(), playerId);

        CharacterAction chosenDirection = lastDirection;
        List<CharacterAction> directions = new ArrayList<>();

        if (!mapUtil.canIMoveInDirection(lastDirection)) {
            directions = Arrays.stream(CharacterAction.values())
                    .filter(mapUtil::canIMoveInDirection)
                    .collect(Collectors.toList());

            // Choose a random direction
            if (!directions.isEmpty())
                chosenDirection = directions.get(random.nextInt(directions.size()));
        }

        registerMove(mapUpdateEvent, chosenDirection);
        lastDirection = chosenDirection;

    }

    private CharacterAction getRandomDirection() {
        int max = CharacterAction.values().length-1;

        return CharacterAction.values()[random.nextInt(max)];
    }
}
