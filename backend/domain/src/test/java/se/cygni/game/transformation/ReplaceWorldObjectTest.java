package se.cygni.game.transformation;

import org.junit.Test;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.exception.TransformationException;
import se.cygni.game.worldobject.Bomb;
import se.cygni.game.worldobject.Empty;

import static org.junit.Assert.assertTrue;

/**
 * @author Alan Tibbetts
 * @since 12/04/16
 */
public class ReplaceWorldObjectTest {

    @Test
    public void testTransform() throws TransformationException {
        WorldState worldState = new WorldState(3, 3);
        Tile tile = worldState.getTile(6);
        assertTrue(tile.getContent() instanceof Empty);

        ReplaceWorldObject replaceWorldObject = new ReplaceWorldObject(new Bomb(), 6);
        WorldState updatedWorldState = replaceWorldObject.transform(worldState);
        Tile updatedTile = updatedWorldState.getTile(6);
        assertTrue(updatedTile.getContent() instanceof Bomb);
    }

    @Test
    public void testTransformSameType() throws TransformationException {
        WorldState worldState = new WorldState(3, 3);
        Tile tile = worldState.getTile(6);
        assertTrue(tile.getContent() instanceof Empty);

        ReplaceWorldObject replaceWorldObject = new ReplaceWorldObject(new Empty(), 6);
        WorldState updatedWorldState = replaceWorldObject.transform(worldState);
        Tile updatedTile = updatedWorldState.getTile(6);
        assertTrue(updatedTile.getContent() instanceof Empty);
    }

    @Test(expected = TransformationException.class)
    public void testTransformWithNull() throws TransformationException {
        WorldState worldState = new WorldState(3, 3);
        Tile tile = worldState.getTile(6);
        assertTrue(tile.getContent() instanceof Empty);

        ReplaceWorldObject replaceWorldObject = new ReplaceWorldObject(null, 6);
        WorldState updatedWorldState = replaceWorldObject.transform(worldState);
    }
}
