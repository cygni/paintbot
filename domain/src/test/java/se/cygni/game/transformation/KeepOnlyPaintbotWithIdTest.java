package se.cygni.game.transformation;

import org.junit.Test;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.exception.TransformationException;
import se.cygni.game.worldobject.CharacterImpl;
import se.cygni.game.worldobject.Empty;

import static org.junit.Assert.*;

/**
 * @author Alan Tibbetts
 * @since 12/04/16
 */
public class KeepOnlyPaintbotWithIdTest {

    @Test
    public void testTransformPaintbotHeadsOnly() throws TransformationException {
        WorldState ws = new WorldState(3, 3);
        Tile[] tiles = ws.getTiles();
        tiles[2] = new Tile(new CharacterImpl("a", "a", 2));
        tiles[6] = new Tile(new CharacterImpl("b", "b", 6));
        tiles[8] = new Tile(new CharacterImpl("b", "b", 6));

        WorldState worldState = ws.withTiles(tiles);

        KeepOnlyPaintbotWithId keepOnlyPaintbotWithId = new KeepOnlyPaintbotWithId("a");
        WorldState updatedWorldState = keepOnlyPaintbotWithId.transform(worldState);

        assertNotNull(updatedWorldState.getCharacterById("a"));

        try {
            updatedWorldState.getCharacterById("b");
            fail("Paintbot 'b' should have been removed from the board");
        } catch (IllegalArgumentException iae) {
            // What we expect!
        }

        try {
            updatedWorldState.getCharacterById("c");
            fail("Paintbot 'c' should have been removed from the board");
        } catch (IllegalArgumentException iae) {
            // What we expect!
        }
    }

    @Test(expected = TransformationException.class)
    public void testTransformCheckingNullId() throws TransformationException {
        WorldState ws = new WorldState(3, 3);
        Tile[] tiles = ws.getTiles();
        tiles[2] = new Tile(new CharacterImpl("a", "a", 2));
        tiles[6] = new Tile(new CharacterImpl("b", "b", 6));
        tiles[8] = new Tile(new CharacterImpl("b", "b", 6));

        WorldState worldState = ws.withTiles(tiles);

        KeepOnlyPaintbotWithId keepOnlyPaintbotWithId = new KeepOnlyPaintbotWithId(null);
        WorldState updatedWorldState = keepOnlyPaintbotWithId.transform(worldState);
    }
}
