package se.cygni.snake.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MapBomb implements TileContent {

    public static final String CONTENT = "bomb";

    @Override
    public String getContent() {
        return CONTENT;
    }

    @JsonIgnore
    public String toDisplay() {
        return "B";
    }
}
