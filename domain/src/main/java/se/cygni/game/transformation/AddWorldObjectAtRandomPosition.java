package se.cygni.game.transformation;

import org.apache.commons.lang3.ArrayUtils;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.game.worldobject.Character;
import se.cygni.game.worldobject.PowerUp;
import se.cygni.game.worldobject.WorldObject;

/**
 * Adds a WorldObject at random free Tile
 */
public class AddWorldObjectAtRandomPosition implements WorldTransformation {

    private final WorldObject worldObject;
    private XORShiftRandom random = new XORShiftRandom();

    public AddWorldObjectAtRandomPosition(WorldObject worldObject) {
        this.worldObject = worldObject;
    }

    @Override
    public WorldState transform(WorldState currentWorld) {

        int[] emptyPositions = currentWorld.listEmptyPositions();
        if (emptyPositions.length == 0) {
            return currentWorld;
        }

        int[] illegalPositions = currentWorld.listPositionsAdjacentToCharacters();

        int[] validPositions = ArrayUtils.removeElements(emptyPositions, illegalPositions);
        if (validPositions.length == 0) {
            return currentWorld;
        }

        int randomPosition = validPositions[random.nextInt(validPositions.length)];

        Tile[] tiles = currentWorld.getTiles();
        Tile currentTile = tiles[randomPosition];
        Tile newTile = new Tile(worldObject);

        if (worldObject instanceof Character) {
            Character character = (Character)worldObject;
            character.setPosition(randomPosition);
        } else if (worldObject instanceof PowerUp) {
            newTile = new Tile(worldObject, currentTile.getOwnerID());
        }

        tiles[randomPosition] = newTile;

        return currentWorld.withTiles(tiles);
    }
}
