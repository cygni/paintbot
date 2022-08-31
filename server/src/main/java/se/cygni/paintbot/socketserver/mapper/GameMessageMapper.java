package se.cygni.paintbot.socketserver.mapper;

import se.cygni.game.WorldState;
import se.cygni.paintbot.api.event.*;
import se.cygni.paintbot.api.model.GameSettings;
import se.cygni.paintbot.api.model.StunReason;
import se.cygni.paintbot.socketserver.game.GameFeatures;
import se.cygni.paintbot.socketserver.game.GameResult;
import se.cygni.paintbot.socketserver.player.IPlayer;

import java.util.Set;

public class GameMessageMapper {

    public static MapUpdateEvent onWorldUpdate(WorldState worldState, String gameId, long gameTick, Set<IPlayer> players) {
        return new MapUpdateEvent(
                gameTick,
                gameId,
                WorldStateConverter.convertWorldState(worldState, gameTick, players));
    }

    public static CharacterStunnedEvent onPlayerStunned(StunReason reason, int durationInTicks, String playerId, int x, int y, String gameId, long gameTick) {
        return new CharacterStunnedEvent(reason, durationInTicks, playerId, x, y, gameId, gameTick);
    }

    public static GameEndedEvent onGameEnded(String playerWinnerId, String playerWinnerName, String gameId, long gameTick, WorldState worldState, Set<IPlayer> players) {
        return new GameEndedEvent(
                playerWinnerId, playerWinnerName, gameId, gameTick,
                WorldStateConverter.convertWorldState(worldState, gameTick, players));
    }

    public GameStartingEvent onGameStart(String gameId, int noofPlayers, int width, int height, GameFeatures gameFeatures) {
        GameSettings settings = GameSettingsMapper.INSTANCE.gameFeaturesToGameSettings(gameFeatures);
        return new GameStartingEvent(gameId, noofPlayers, width, height, settings);
    }

    public static GameAbortedEvent onGameAborted(String gameId) {
        return new GameAbortedEvent(gameId);
    }

    public static GameChangedEvent onGameChanged(String gameId) {
        return new GameChangedEvent(gameId);
    }

    public static GameResultEvent onGameResult(String gameId, GameResult gameResult) {
        return new GameResultEvent(gameId, GameResultMapper.getPlayerRanks(gameResult));
    }
}
