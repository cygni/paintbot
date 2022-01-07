package se.cygni.paintbot.event;

import se.cygni.paintbot.api.GameMessage;
import se.cygni.paintbot.apiconversion.GameMessageConverter;

public class InternalGameEvent {
    private final long tstamp;
    private GameMessage gameMessage;

    private final boolean isTraining;

    public InternalGameEvent(long tstamp, boolean isTraining) {
        this.tstamp = tstamp;
        this.isTraining = isTraining;
    }

    public InternalGameEvent(long tstamp, GameMessage gameMessage, boolean isTraining) {
        this.tstamp = tstamp;
        this.gameMessage = gameMessage;
        this.isTraining = isTraining;
    }

    public long getTstamp() {
        return tstamp;
    }

    public GameMessage getGameMessage() {
        return gameMessage;
    }

    public void onGameAborted(String gameId) {
        this.gameMessage = GameMessageConverter.onGameAborted(gameId);
    }

    public void onGameChanged(String gameId) {
        this.gameMessage = GameMessageConverter.onGameChanged(gameId);
    }

    public boolean isTraining() {
        return isTraining;
    }
}
