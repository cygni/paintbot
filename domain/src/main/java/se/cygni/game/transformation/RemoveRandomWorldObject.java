package se.cygni.game.transformation;

import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.game.worldobject.Empty;
import se.cygni.game.worldobject.WorldObject;

/**
 * Adds a PowerUp object at random free Tile
 */
public class RemoveRandomWorldObject<T extends WorldObject> implements WorldTransformation {

    private final Class<T> worldObjectType;
    private XORShiftRandom random = new XORShiftRandom();

    public RemoveRandomWorldObject(Class<T> worldObjectType) {
        this.worldObjectType = worldObjectType;
    }

    @Override
    public WorldState transform(WorldState currentWorld) {

        int[] positionsWithContentOfType = currentWorld.listPositionsWithContentOf(worldObjectType);
        if (positionsWithContentOfType.length == 0)
            return currentWorld;

        int randomPosition = positionsWithContentOfType[random.nextInt(positionsWithContentOfType.length)];

        Tile[] tiles = currentWorld.getTiles();
        Tile currentTile = tiles[randomPosition];
        tiles[randomPosition] = new Tile(new Empty(), currentTile.getOwnerID());

        return currentWorld.withTiles(tiles);
    }
}
