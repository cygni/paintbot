package se.cygni.paintbot.player.bot;

import com.google.common.eventbus.EventBus;
import se.cygni.paintbot.api.event.*;
import se.cygni.paintbot.api.model.CharacterAction;
import se.cygni.paintbot.api.model.Map;
import se.cygni.paintbot.api.model.TileContent;
import se.cygni.paintbot.api.request.RegisterMove;
import se.cygni.paintbot.client.MapCoordinate;
import se.cygni.paintbot.client.MapUtilityImpl;
import se.cygni.paintbot.player.BasePlayer;

public abstract class BotPlayer extends BasePlayer {

    private boolean alive = true;
    protected final String playerId;
    protected final EventBus incomingEventbus;
    private int accumulatedPoints = 0;

    public BotPlayer(String playerId, EventBus incomingEventbus) {
        this.playerId = playerId;
        this.incomingEventbus = incomingEventbus;
    }

    public void registerMove(MapUpdateEvent mapUpdateEvent, CharacterAction action) {
        RegisterMove registerMove = new RegisterMove(mapUpdateEvent.getGameId(), mapUpdateEvent.getGameTick(), action);
        registerMove.setReceivingPlayerId(playerId);
        incomingEventbus.post(registerMove);
    }

    @Override
    public void onWorldUpdate(MapUpdateEvent mapUpdateEvent) {

    }

    @Override
    public void onCharacterStunned(CharacterStunnedEvent characterStunnedEvent) {

    }

    @Override
    public void onGameEnded(GameEndedEvent gameEndedEvent) {

    }

    @Override
    public void onGameResult(GameResultEvent gameResultEvent) {

    }

    @Override
    public void onTournamentEnded(TournamentEndedEvent tournamentEndedEvent) {

    }

    @Override
    public void onGameStart(GameStartingEvent gameStartingEvent) {

    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Adds scores for 1 to n tiles in the indicated direction, starting from where we are now.
     *
     * @param mapUtil current MapUtil class
     * @param potentialDirection which direction are we looking?
     * @param myPosition where am I now?
     * @param howFar how far can I see?
     */
    protected void addDirectionScore(final Map gameMap, final MapUtilityImpl mapUtil, final PotentialDirection potentialDirection, final MapCoordinate myPosition, final int howFar) {
        for (int i = 1; i <= howFar; i++) {
            MapCoordinate coordinate = possibleNewPosition(myPosition, potentialDirection.getDirection(), i);
            addTileToPotentialDirection(gameMap, mapUtil, potentialDirection, i, coordinate);

            if (i == 1) {
                if (potentialDirection.getDirection() == CharacterAction.LEFT || potentialDirection.getDirection() == CharacterAction.RIGHT) {
                    coordinate = coordinate.translateBy(1, 0);
                    addTileToPotentialDirection(gameMap, mapUtil, potentialDirection, i, coordinate);
                    coordinate = coordinate.translateBy(-1, 0);
                    addTileToPotentialDirection(gameMap, mapUtil, potentialDirection, i, coordinate);
                } else {
                    coordinate = coordinate.translateBy(0, 1);
                    addTileToPotentialDirection(gameMap, mapUtil, potentialDirection, i, coordinate);
                    coordinate = coordinate.translateBy(0, -1);
                    addTileToPotentialDirection(gameMap, mapUtil, potentialDirection, i, coordinate);
                }
            }
        }
    }

    private void addTileToPotentialDirection(Map gameMap, MapUtilityImpl mapUtil, PotentialDirection potentialDirection, int i, MapCoordinate coordinate) {
        if (mapUtil.isCoordinateOutOfBounds(coordinate)) {
            potentialDirection.applyOutOfBoundsScore();
        } else {
            TileContent tileContent = mapUtil.getTileAt(coordinate);
            potentialDirection.addTile(gameMap, tileContent, i, getPlayerId());
        }
    }

    /**
     * @param currentPosition paintbot's current coordinates
     * @param direction       direction the paintbot wants to investigate
     * @param howFar          how many tiles from the paintbot's current position
     * @return potential new coordinate
     */
    protected MapCoordinate possibleNewPosition(final MapCoordinate currentPosition, final CharacterAction direction, final int howFar) {
        MapCoordinate newCoordinate = currentPosition.translateBy(0, 0);
        switch (direction) {
            case DOWN:
                newCoordinate = newCoordinate.translateBy(0, howFar);
                break;
            case UP:
                newCoordinate = newCoordinate.translateBy(0, -howFar);
                break;
            case LEFT:
                newCoordinate = newCoordinate.translateBy(-howFar, 0);
                break;
            case RIGHT:
                newCoordinate = newCoordinate.translateBy(howFar, 0);
        }
        return newCoordinate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BotPlayer botPlayer = (BotPlayer) o;

        return playerId != null ? playerId.equals(botPlayer.playerId) : botPlayer.playerId == null;

    }

    @Override
    public int hashCode() {
        return playerId != null ? playerId.hashCode() : 0;
    }
}
