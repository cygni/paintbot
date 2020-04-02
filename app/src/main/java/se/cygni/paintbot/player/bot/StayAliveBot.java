package se.cygni.paintbot.player.bot;

import com.google.common.eventbus.EventBus;
import se.cygni.paintbot.api.event.MapUpdateEvent;
import se.cygni.paintbot.api.model.Map;
import se.cygni.paintbot.api.model.CharacterAction;
import se.cygni.paintbot.api.request.RegisterMove;
import se.cygni.paintbot.client.MapCoordinate;
import se.cygni.paintbot.client.MapUtilityImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strategy:
 * : will look at the tiles directly UP,LEFT,RIGHT and DOWN, to a maximum of HOW_MANY_TILES_CAN_I_SEE
 * : possible directions will be scored, based on the contents of the tiles
 * : closer tiles are given higher weighting
 * : food is not more attractive than empty spaces
 * : will choose the direction with the highest score
 *
 * @author Alan Tibbetts
 * @since 14/04/16
 */
public class StayAliveBot extends BotPlayer {

    private static final int HOW_MANY_TILES_CAN_I_SEE = 3;

    private CharacterAction currentDirection = null;

    public StayAliveBot(String playerId, EventBus incomingEventbus) {
        super(playerId, incomingEventbus);
    }

    @Override
    public void onWorldUpdate(MapUpdateEvent mapUpdateEvent) {
        CompletableFuture cf = CompletableFuture.runAsync(() -> {
            postNextMove(mapUpdateEvent.getGameId(), mapUpdateEvent.getMap(), mapUpdateEvent.getGameTick());
        });
    }

    private void postNextMove(final String gameId, final Map map, final long gameTick) {
        MapUtilityImpl mapUtil = new MapUtilityImpl(map, getPlayerId());

        List<PotentialDirection> directions = createDirections(map, mapUtil);
        if (directions.size() > 0) {
            currentDirection = directions.get(0).getDirection();
        }

        RegisterMove registerMove = new RegisterMove(gameId, gameTick, currentDirection);
        registerMove.setReceivingPlayerId(playerId);
        incomingEventbus.post(registerMove);
    }

    private List<PotentialDirection> createDirections(Map gameMap, MapUtilityImpl mapUtil) {
        MapCoordinate myPosition = mapUtil.getMyPosition();
        List<PotentialDirection> directions = new ArrayList<>(4);

        for (CharacterAction characterAction : PotentialDirection.POSSIBLE_DIRECTIONS) {
            if (!PotentialDirection.isOppositeDirection(currentDirection, characterAction) && !isDirectionBlocked(mapUtil, characterAction)) {
                PotentialDirection potentialDirection = new PotentialDirection(characterAction);
                if (currentDirection != null && currentDirection == characterAction) {
                    potentialDirection.goingThisWayAnyway();
                }
                directions.add(potentialDirection);
                addDirectionScore(gameMap, mapUtil, potentialDirection, myPosition, HOW_MANY_TILES_CAN_I_SEE);
            }
        }

        Collections.sort(directions);
        Collections.reverse(directions);

        return directions;
    }

    private boolean isDirectionBlocked(final MapUtilityImpl mapUtil, final CharacterAction characterAction) {
        MapCoordinate coordinate = possibleNewPosition(mapUtil.getMyPosition(), characterAction, 1);
        return !mapUtil.isTileAvailableForMovementTo(coordinate);
    }
}
