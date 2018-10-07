package se.cygni.snake.player.bot;

import com.google.common.eventbus.EventBus;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.snake.api.event.MapUpdateEvent;
import se.cygni.snake.api.model.CharacterAction;
import se.cygni.snake.api.model.Map;
import se.cygni.snake.api.request.RegisterMove;
import se.cygni.snake.client.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RandomBot extends BotPlayer {

    private CharacterAction myLastDirection;
    private XORShiftRandom random = new XORShiftRandom();

    public RandomBot(String playerId, EventBus incomingEventbus) {
        super(playerId, incomingEventbus);
    }

    @Override
    public void onWorldUpdate(MapUpdateEvent mapUpdateEvent) {
        CompletableFuture cf = CompletableFuture.runAsync(() -> {
            postNextMove(mapUpdateEvent.getGameId(), mapUpdateEvent.getMap(), mapUpdateEvent.getGameTick());
        });
    }

    private void postNextMove(String gameId, Map map, long gameTick) {

        MapUtil mapUtil = new MapUtil(map, playerId);

        CharacterAction rndDirection = getRandomDirection();
        List<CharacterAction> validDirections = getValidDirections(mapUtil);
        if (validDirections.size() > 0) {
            rndDirection = getRandomDirection(validDirections);
        }
        myLastDirection = rndDirection;

        RegisterMove registerMove = new RegisterMove(gameId, gameTick, rndDirection);
        registerMove.setReceivingPlayerId(playerId);
        incomingEventbus.post(registerMove);
    }

    private List<CharacterAction> getValidDirections(MapUtil mapUtil) {

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
