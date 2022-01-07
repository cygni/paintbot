package se.cygni.paintbot.eventapi.history;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;
import se.cygni.paintbot.api.GameMessage;
import se.cygni.paintbot.eventapi.ApiMessage;
import se.cygni.paintbot.eventapi.type.ApiMessageType;

import java.time.LocalDateTime;
import java.util.List;

@ApiMessageType
public class GameHistory extends ApiMessage {

    private final String gameId;

    private final String[] playerNames;

    private final LocalDateTime gameDate;

    private final List<GameMessage> messages;

    private final boolean isTrainingGame;
    @JsonCreator
    public GameHistory(
            @JsonProperty("gameId") String gameId,
            @JsonProperty("playerNames") String[] playerNames,
            @JsonProperty("gameDate") LocalDateTime gameDate,
            @JsonProperty("messages") List<GameMessage> messages,
            @JsonProperty("isTrainingGame") boolean isTrainingGame) {

        this.gameId = gameId;
        this.playerNames = playerNames;
        this.gameDate = gameDate;
        this.messages = messages;
        this.isTrainingGame = isTrainingGame;
    }

    public String getGameId() {
        return gameId;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalDateTime getGameDate() {
        return gameDate;
    }

    public List<GameMessage> getMessages() {
        return messages;
    }

    public boolean isTrainingGame() {
        return isTrainingGame;
    }
}
