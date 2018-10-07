package se.cygni.snake.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterInfo {

    final String name;
    final int points;
    final String id;
    final int position;
    final int[] colouredPositions;
    final int stunnedForGameTicks;
    //TODO: Also include invulnerable state ticks?

    @JsonCreator
    public CharacterInfo(
            @JsonProperty("name") String name,
            @JsonProperty("points") int points,
            @JsonProperty("playerId") String playerId,
            @JsonProperty("position") int position,
            @JsonProperty("colouredPositions") int[] colouredPositions,
            @JsonProperty("stunnedForGameTicks") int stunnedForGameTicks
    )
    {
        this.name = name;
        this.points = points;
        this.id = playerId;
        this.position = position;
        this.colouredPositions = colouredPositions;
        this.stunnedForGameTicks = stunnedForGameTicks;
    }

    public String getName() {
        return name;
    }


    public int getPoints() {
        return points;
    }

    public String getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public int getStunnedForGameTicks() {
        return stunnedForGameTicks;
    }

    public int[] getColouredPositions() {
        return colouredPositions;
    }
}
