package se.cygni.snake.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.cygni.snake.api.GameMessage;
import se.cygni.snake.api.model.StunReason;
import se.cygni.snake.api.type.GameMessageType;

@GameMessageType
public class CharacterStunnedEvent extends GameMessage {

    private final StunReason stunReason;
    private final int durationInTicks;
    private final String playerId;
    private final int x, y;
    private final String gameId;
    private final long gameTick;


    @JsonCreator
    public CharacterStunnedEvent(
            @JsonProperty("stunReason") StunReason stunReason,
            @JsonProperty("durationInTicks") int durationInTicks,
            @JsonProperty("playerId") String playerId,
            @JsonProperty("x") int x,
            @JsonProperty("y") int y,
            @JsonProperty("gameId") String gameId,
            @JsonProperty("gameTick") long gameTick) {

        this.stunReason = stunReason;
        this.durationInTicks = durationInTicks;
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.gameId = gameId;
        this.gameTick = gameTick;
    }

    public CharacterStunnedEvent(CharacterStunnedEvent cse) {
        this.stunReason = cse.getStunReason();
        this.durationInTicks = cse.getDurationInTicks();
        this.playerId = cse.getPlayerId();
        this.x = cse.getX();
        this.y = cse.getY();
        this.gameId = cse.getGameId();
        this.gameTick = cse.getGameTick();
    }

    /**
     *
     * @return the reason for this death
     */
    public StunReason getStunReason() {
        return stunReason;
    }

    /**
     *
     * @return id of the snake that died
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     *
     * @return x coordinate for where the snake head collided
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return y coordinate for where the snake head collided
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return gameId
     */
    public String getGameId() {
        return gameId;
    }

    /**
     *
     * @return gameTick
     */
    public long getGameTick() {
        return gameTick;
    }

    @Override
    public String toString() {
        return "CharacterStunnedEvent{" +
                "stunReason=" + stunReason +
                ", playerId='" + playerId + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", gameId='" + gameId + '\'' +
                ", gameTick=" + gameTick +
                ", durationInTicks=" + durationInTicks +
                '}';
    }

    public int getDurationInTicks() {
        return durationInTicks;
    }
}
