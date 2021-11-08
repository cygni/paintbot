package se.cygni.paintbot.client;

import se.cygni.paintbot.api.event.*;
import se.cygni.paintbot.api.exception.InvalidPlayerName;
import se.cygni.paintbot.api.model.GameMode;
import se.cygni.paintbot.api.response.PlayerRegistered;

public interface PaintbotClient {

    void onMapUpdate(MapUpdateEvent mapUpdateEvent);

    void onPaintbotDead(CharacterStunnedEvent characterStunnedEvent);

    void onGameResult(GameResultEvent gameResultEvent);

    void onGameEnded(GameEndedEvent gameEndedEvent);

    void onGameStarting(GameStartingEvent gameStartingEvent);

    void onTournamentEnded(TournamentEndedEvent tournamentEndedEvent);

    void onPlayerRegistered(PlayerRegistered playerRegistered);

    void onInvalidPlayerName(InvalidPlayerName invalidPlayerName);

    void onGameLink(GameLinkEvent gameLinkEvent);

    String getProtocol();

    String getServerHost();

    int getServerPort();

    void onConnected();

    void onSessionClosed();

    String getName();

    GameMode getGameMode();

}
