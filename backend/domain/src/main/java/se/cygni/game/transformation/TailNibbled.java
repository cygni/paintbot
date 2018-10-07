package se.cygni.game.transformation;

import org.apache.commons.lang3.StringUtils;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.exception.TransformationException;
import se.cygni.game.worldobject.SnakeHead;
import se.cygni.game.worldobject.SnakePart;

/**
 * Adds a Food object at random free Tile
 */
public class TailNibbled implements WorldTransformation {

    private final String snakeId;
    private final int position;
    private final int protectedForTicks;

    public TailNibbled(String snakeId, int position, int protectedForTicks) {
        this.snakeId = snakeId;
        this.position = position;
        this.protectedForTicks = protectedForTicks;
    }

    @Override
    public WorldState transform(WorldState currentWorld) throws TransformationException {
        if (StringUtils.isEmpty(snakeId)) {
            String errorMessage = String.format("Can't nibble the tail of a snake with missing id: %s", snakeId);
            throw new TransformationException(errorMessage);
        }

        SnakeHead head = currentWorld.getSnakeHeadById(snakeId);
        int[] snakeSpread = currentWorld.getSnakeSpread(head);

        if (snakeSpread[snakeSpread.length - 1] != position) {
            throw new TransformationException("Supplied position was not the snakes tail");
        }

        Tile[] tiles = currentWorld.getTiles();
        tiles[position] = new Tile();

        int previousPartPosition = snakeSpread[snakeSpread.length - 2];
        SnakePart previousPart = (SnakePart) tiles[previousPartPosition].getContent();
        previousPart.setNextSnakePart(null);

        head.setTailProtectedForGameTicks(protectedForTicks + 1);
        return new WorldState(currentWorld.getWidth(), currentWorld.getHeight(), tiles);
    }
}
