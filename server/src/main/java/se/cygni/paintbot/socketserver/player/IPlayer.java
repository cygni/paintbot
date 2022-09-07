package se.cygni.paintbot.socketserver.player;

import se.cygni.paintbot.api.GameMessage;
import se.cygni.paintbot.api.event.*;
import se.cygni.paintbot.api.model.PointReason;

public interface IPlayer extends Comparable<IPlayer> {

    default void onGameMessage(GameMessage gameMessage) {
        if (gameMessage instanceof  MapUpdateEvent) {
            onWorldUpdate((MapUpdateEvent)gameMessage);
        } else if (gameMessage instanceof CharacterStunnedEvent) {
            onCharacterStunned((CharacterStunnedEvent)gameMessage);
        } else if (gameMessage instanceof  GameResultEvent) {
            onGameResult((GameResultEvent)gameMessage);
        } else if (gameMessage instanceof  GameEndedEvent) {
            onGameEnded((GameEndedEvent)gameMessage);
        } else if (gameMessage instanceof  GameStartingEvent) {
            onGameStart((GameStartingEvent)gameMessage);
        } else if (gameMessage instanceof  TournamentEndedEvent) {
            onTournamentEnded((TournamentEndedEvent)gameMessage);
        }
    }

    void onWorldUpdate(MapUpdateEvent mapUpdateEvent);

    void onCharacterStunned(CharacterStunnedEvent characterStunnedEvent);

    void onGameResult(GameResultEvent gameResultEvent);

    void onGameEnded(GameEndedEvent gameEndedEvent);

    void onGameStart(GameStartingEvent gameStartingEvent);

    void onTournamentEnded(TournamentEndedEvent tournamentEndedEvent);

    void lostConnection(long gameTick);

    boolean isAlive();

    long getDiedAtTick();

    boolean isConnected();

    boolean isInTournament();

    void outOfTournament();

    void stunned(long gameTick);

    void revive();

    String getName();

    String getPlayerId();

    void addPoints(PointReason reason, int points);

    void reset();

    int getTotalPoints();

    int getPointsBy(PointReason reason);
}
