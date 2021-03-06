package se.cygni.paintbot.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.cygni.paintbot.api.GameMessage;
import se.cygni.paintbot.api.model.Map;
import se.cygni.paintbot.api.type.GameMessageType;

@GameMessageType
public class GameEndedEvent extends GameMessage {

    private final String playerWinnerId;
    private final String playerWinnerName;
    private final String gameId;
    private final long gameTick;
    private final Map map;


    @JsonCreator
    public GameEndedEvent(
            @JsonProperty("playerWinnerId") String playerWinnerId,
            @JsonProperty("playerWinnerName") String playerWinnerName,
            @JsonProperty("gameId") String gameId,
            @JsonProperty("gameTick") long gameTick,
            @JsonProperty("map") Map map) {

        this.playerWinnerId = playerWinnerId;
        this.playerWinnerName = playerWinnerName;
        this.gameId = gameId;
        this.gameTick = gameTick;
        this.map = map;
    }

    public GameEndedEvent(GameEndedEvent gee) {
        this.playerWinnerId = gee.getPlayerWinnerId();
        this.playerWinnerName = gee.getPlayerWinnerName();
        this.gameId = gee.getGameId();
        this.gameTick = gee.getGameTick();
        this.map = gee.getMap();
    }

    public String getPlayerWinnerId() {
        return playerWinnerId;
    }

    public String getPlayerWinnerName() {
        return playerWinnerName;
    }

    public String getGameId() {
        return gameId;
    }

    public long getGameTick() {
        return gameTick;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "GameEndedEvent{" +
                "playerWinnerId='" + playerWinnerId + '\'' +
                ", gameId='" + gameId + '\'' +
                ", gameTick=" + gameTick +
                ", map=\n" + map +
                '}';
    }
}
