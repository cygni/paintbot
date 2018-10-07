package se.cygni.snake.player.bot;

import com.google.common.eventbus.EventBus;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.snake.api.event.MapUpdateEvent;
import se.cygni.snake.api.model.CharacterAction;
import se.cygni.snake.api.model.Map;
import se.cygni.snake.api.request.RegisterMove;
import se.cygni.snake.client.MapUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class StraightBot extends BotPlayer {

    CharacterAction lastDirection;
    private XORShiftRandom random = new XORShiftRandom();

    public StraightBot(String playerId, EventBus incomingEventbus) {
        super(playerId, incomingEventbus);
        lastDirection = getRandomDirection();
    }

    @Override
    public void onWorldUpdate(MapUpdateEvent mapUpdateEvent) {
        CompletableFuture cf = CompletableFuture.runAsync(() -> {

            postNextMove(mapUpdateEvent.getGameId(), mapUpdateEvent.getMap(), mapUpdateEvent.getGameTick());
        });
    }


    private void postNextMove(String gameId, Map map, long gameTick) {

        // MapUtil contains lot's of useful methods for querying the map!
        MapUtil mapUtil = new MapUtil(map, playerId);


        CharacterAction chosenDirection = lastDirection;
        List<CharacterAction> directions = new ArrayList<>();

        if (!mapUtil.canIMoveInDirection(lastDirection)) {
            directions = Arrays.stream(CharacterAction.values()).filter(direction ->
                mapUtil.canIMoveInDirection(direction)
            ).collect(Collectors.toList());

            // Choose a random direction
            if (!directions.isEmpty())
                chosenDirection = directions.get(random.nextInt(directions.size()));
        }

        // Register action here!
        RegisterMove registerMove = new RegisterMove(gameId, gameTick, chosenDirection);
        registerMove.setReceivingPlayerId(playerId);
        incomingEventbus.post(registerMove);
        lastDirection = chosenDirection;

    }

    private CharacterAction getRandomDirection() {
        int max = CharacterAction.values().length-1;

        return CharacterAction.values()[random.nextInt(max)];
    }
}
