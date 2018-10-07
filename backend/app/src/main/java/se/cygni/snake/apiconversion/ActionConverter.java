package se.cygni.snake.apiconversion;

import se.cygni.game.enums.Action;
import se.cygni.snake.api.model.SnakeDirection;

public class DirectionConverter {

    public static Action toDirection(SnakeDirection snakeDirection) {
        switch (snakeDirection) {
            case UP: return Action.UP;
            case DOWN: return Action.DOWN;
            case LEFT: return Action.LEFT;
            case RIGHT: return Action.RIGHT;
        }
        throw new RuntimeException("Could not convert SnakeDirection: " + snakeDirection + " to Action");
    }

    public static SnakeDirection toSnakeDirection(Action action) {
        switch (action) {
            case UP:    return SnakeDirection.UP;
            case DOWN:  return SnakeDirection.DOWN;
            case LEFT:  return SnakeDirection.LEFT;
            case RIGHT: return SnakeDirection.RIGHT;
        }
        throw new RuntimeException("Could not convert Action: " + action + " to SnakeDirection");
    }
}
