package se.cygni.paintbot.eventapi.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.cygni.paintbot.eventapi.ApiMessage;
import se.cygni.paintbot.eventapi.type.ApiMessageType;

import java.util.List;
import java.util.Map;

@ApiMessageType
public class ArenaUpdateEvent extends ApiMessage {
    private final String arenaName;
    private final String gameId;
    private final Boolean ranked;
    private final List<String> onlinePlayers;
    private final Map<String, Long> rating;
    private final List<se.cygni.paintbot.api.event.ArenaUpdateEvent.ArenaHistory> gameHistory;

    public static class ArenaHistory {
        public ArenaHistory(String gameId, List<String> playerPositions) {
            this.gameId = gameId;
            this.playerPositions = playerPositions;
        }

        private final String gameId;
        private final List<String> playerPositions;

        public String getGameId() {
            return gameId;
        }

        public List<String> getPlayerPositions() {
            return playerPositions;
        }
    }

    @JsonCreator
    public ArenaUpdateEvent(
            @JsonProperty("arenaName") String arenaName,
            @JsonProperty("gameId") String gameId,
            @JsonProperty("ranked") Boolean ranked,
            @JsonProperty("rating") Map<String, Long> rating,
            @JsonProperty("onlinePlayers") List<String> onlinePlayers,
            @JsonProperty("gameHistory") List<se.cygni.paintbot.api.event.ArenaUpdateEvent.ArenaHistory> gameHistory) {

        this.arenaName = arenaName;
        this.gameId = gameId;
        this.ranked = ranked;
        this.rating = rating;
        this.onlinePlayers = onlinePlayers;
        this.gameHistory = gameHistory;
    }

    public ArenaUpdateEvent(se.cygni.paintbot.api.event.ArenaUpdateEvent other) {
        this.arenaName = other.getArenaName();
        this.gameId = other.getGameId();
        this.ranked = other.getRanked();
        this.rating = other.getRating();
        this.gameHistory = other.getGameHistory();

        this.onlinePlayers = other.getOnlinePlayers();
    }

    public String getArenaName() {
        return arenaName;
    }

    public String getGameId() {
        return gameId;
    }

    public Boolean getRanked() {
        return ranked;
    }

    public Map<String, Long> getRating() {
        return rating;
    }

    public List<String> getOnlinePlayers() {
        return onlinePlayers;
    }

    public List<se.cygni.paintbot.api.event.ArenaUpdateEvent.ArenaHistory> getGameHistory() {
        return gameHistory;
    }
}
