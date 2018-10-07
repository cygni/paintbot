package se.cygni.snake.apiconversion;

import se.cygni.game.enums.Action;
import se.cygni.snake.api.model.CharacterAction;

public class ActionConverter {

    public static Action toDirection(CharacterAction characterAction) {
        switch (characterAction) {
            case UP: return Action.UP;
            case DOWN: return Action.DOWN;
            case LEFT: return Action.LEFT;
            case RIGHT: return Action.RIGHT;
            case STAY: return Action.STAY;
            case EXPLODE: return Action.EXPLODE;
        }
        throw new RuntimeException("Could not convert CharacterAction: " + characterAction + " to Action");
    }

    public static CharacterAction toSnakeDirection(Action action) {
        switch (action) {
            case UP:    return CharacterAction.UP;
            case DOWN:  return CharacterAction.DOWN;
            case LEFT:  return CharacterAction.LEFT;
            case RIGHT: return CharacterAction.RIGHT;
            case STAY: return CharacterAction.STAY;
            case EXPLODE: return CharacterAction.EXPLODE;
        }
        throw new RuntimeException("Could not convert Action: " + action + " to CharacterAction");
    }
}
