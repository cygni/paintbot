package se.cygni.game.transformation;

import org.junit.Test;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.worldobject.CharacterImpl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alan Tibbetts
 * @since 12/04/16
 */
public class DecrementStunTest {

    @Test
    public void testTransform() {
        CharacterImpl paintbotA = new CharacterImpl("a", "a", 2);
        paintbotA.setIsStunnedForTicks(3);

        WorldState ws = new WorldState(3, 3);
        Tile[] tiles = ws.getTiles();
        tiles[2] = new Tile(paintbotA);
        WorldState worldState = ws.withTiles(tiles);

        DecrementStun decrementStun = new DecrementStun();

        WorldState updatedWorldState = decrementStun.transform(worldState);
        CharacterImpl updatedCharacter = updatedWorldState.getCharacterById("a");
        assertEquals(2, updatedCharacter.getIsStunnedForTicks());
    }

    @Test
    public void testTransformWithZeroCount() {
        CharacterImpl paintbotA = new CharacterImpl("a", "a", 2);

        WorldState ws = new WorldState(3, 3);
        Tile[] tiles = ws.getTiles();
        tiles[2] = new Tile(paintbotA);
        WorldState worldState = ws.withTiles(tiles);

        DecrementStun decrementStun = new DecrementStun();

        WorldState updatedWorldState = decrementStun.transform(worldState);
        CharacterImpl updatedCharacter = updatedWorldState.getCharacterById("a");
        assertEquals(0, updatedCharacter.getIsStunnedForTicks());
    }

    @Test
    public void testTransformWithoutTouchingCollisionsOrExplosions() {
        CharacterImpl paintbotA = new CharacterImpl("a", "a", 2);

        WorldState ws = new WorldState(3, 3);
        Tile[] tiles = ws.getTiles();
        Map<Integer, List<String>> collisions = new HashMap<>();
        collisions.put(0, List.of("a"));
        Map<Integer, List<String>> explosions = new HashMap<>();
        explosions.put(0, List.of("a"));
        WorldState worldState = ws.withTiles(tiles).withCollisions(collisions).withExplosions(explosions);

        DecrementStun decrementStun = new DecrementStun();

        WorldState updatedWorldState = decrementStun.transform(worldState);
        assertEquals(1, updatedWorldState.getCollisions().size());
        assertEquals(1, updatedWorldState.getCollisions().get(0).size());
        assertEquals("a", updatedWorldState.getCollisions().get(0).get(0));
        assertEquals(1, updatedWorldState.getExplosions().size());
        assertEquals(1, updatedWorldState.getExplosions().get(0).size());
        assertEquals("a", updatedWorldState.getExplosions().get(0).get(0));
    }
}
