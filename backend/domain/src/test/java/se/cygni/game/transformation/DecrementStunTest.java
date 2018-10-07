package se.cygni.game.transformation;

import org.junit.Test;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.worldobject.CharacterImpl;

import static org.junit.Assert.assertEquals;

/**
 * @author Alan Tibbetts
 * @since 12/04/16
 */
public class DecrementTailProtectionTest {

    @Test
    public void testTransform() {
        CharacterImpl snakeA = new CharacterImpl("a", "a", 2);
        snakeA.setTailProtectedForGameTicks(3);

        Tile[] tiles = new WorldState(3, 3).getTiles();
        tiles[2] = new Tile(snakeA);
        WorldState worldState = new WorldState(3, 3, tiles);

        DecrementTailProtection decrementTailProtection = new DecrementTailProtection();

        WorldState updatedWorldState = decrementTailProtection.transform(worldState);
        CharacterImpl updatedCharacter = updatedWorldState.getCharacterById("a");
        assertEquals(2, updatedCharacter.getTailProtectedForGameTicks());
    }

    @Test
    public void testTransformWithZeroCount() {
        CharacterImpl snakeA = new CharacterImpl("a", "a", 2);

        Tile[] tiles = new WorldState(3, 3).getTiles();
        tiles[2] = new Tile(snakeA);
        WorldState worldState = new WorldState(3, 3, tiles);

        DecrementTailProtection decrementTailProtection = new DecrementTailProtection();

        WorldState updatedWorldState = decrementTailProtection.transform(worldState);
        CharacterImpl updatedCharacter = updatedWorldState.getCharacterById("a");
        assertEquals(0, updatedCharacter.getTailProtectedForGameTicks());
    }
}
