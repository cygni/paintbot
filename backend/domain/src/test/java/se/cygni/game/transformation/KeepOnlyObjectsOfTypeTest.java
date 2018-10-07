package se.cygni.game.transformation;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.exception.TransformationException;
import se.cygni.game.worldobject.Bomb;
import se.cygni.game.worldobject.CharacterImpl;
import se.cygni.game.worldobject.Empty;
import se.cygni.game.worldobject.Obstacle;

import static org.junit.Assert.assertTrue;

/**
 * @author Alan Tibbetts
 * @since 12/04/16
 */
public class KeepOnlyObjectsOfTypeTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testTransformWithFoodOnly() throws TransformationException {
        CharacterImpl snakeA = new CharacterImpl("a", "a", 2);
        snakeA.setTailProtectedForGameTicks(3);

        Tile[] tiles = new WorldState(3, 3).getTiles();
        tiles[2] = new Tile(snakeA);
        tiles[7] = new Tile(new Bomb());
        WorldState worldState = new WorldState(3, 3, tiles);

        Class[] beforeClasses = {Empty.class, Bomb.class, CharacterImpl.class};
        Class[] keepClasses = {Bomb.class};
        Class[] afterClasses = {Empty.class, Bomb.class};

        for (Tile tile : worldState.getTiles()) {
            assertTrue(ArrayUtils.contains(beforeClasses, tile.getContent().getClass()));
        }

        KeepOnlyObjectsOfType keepOnlyObjectsOfType = new KeepOnlyObjectsOfType(keepClasses);
        WorldState updatedWorldState = keepOnlyObjectsOfType.transform(worldState);
        for (Tile tile : updatedWorldState.getTiles()) {
            assertTrue(ArrayUtils.contains(afterClasses, tile.getContent().getClass()));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testTransformWithFoodAndObstacles() throws TransformationException {
        CharacterImpl snakeA = new CharacterImpl("a", "a", 2);
        snakeA.setTailProtectedForGameTicks(3);

        Tile[] tiles = new WorldState(3, 3).getTiles();
        tiles[2] = new Tile(snakeA);
        tiles[7] = new Tile(new Bomb());
        tiles[8] = new Tile(new Obstacle());

        WorldState worldState = new WorldState(3, 3, tiles);

        Class[] beforeClasses = {Empty.class, Bomb.class, Obstacle.class, CharacterImpl.class};
        Class[] keepClasses = {Bomb.class, Obstacle.class};
        Class[] afterClasses = {Empty.class, Bomb.class, Obstacle.class};

        for (Tile tile : worldState.getTiles()) {
            assertTrue(ArrayUtils.contains(beforeClasses, tile.getContent().getClass()));
        }

        KeepOnlyObjectsOfType keepOnlyObjectsOfType = new KeepOnlyObjectsOfType(keepClasses);
        WorldState updatedWorldState = keepOnlyObjectsOfType.transform(worldState);
        for (Tile tile : updatedWorldState.getTiles()) {
            assertTrue(ArrayUtils.contains(afterClasses, tile.getContent().getClass()));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testTransformWithEmptyList() throws TransformationException {
        CharacterImpl snakeA = new CharacterImpl("a", "a", 2);
        snakeA.setTailProtectedForGameTicks(3);

        Tile[] tiles = new WorldState(3, 3).getTiles();
        tiles[2] = new Tile(snakeA);
        tiles[7] = new Tile(new Bomb());
        tiles[8] = new Tile(new Obstacle());

        WorldState worldState = new WorldState(3, 3, tiles);

        Class[] beforeClasses = {Empty.class, Bomb.class, Obstacle.class, CharacterImpl.class};
        Class[] keepClasses = {};
        Class[] afterClasses = {Empty.class};

        for (Tile tile : worldState.getTiles()) {
            assertTrue(ArrayUtils.contains(beforeClasses, tile.getContent().getClass()));
        }

        KeepOnlyObjectsOfType keepOnlyObjectsOfType = new KeepOnlyObjectsOfType(keepClasses);
        WorldState updatedWorldState = keepOnlyObjectsOfType.transform(worldState);
        for (Tile tile : updatedWorldState.getTiles()) {
            assertTrue(ArrayUtils.contains(afterClasses, tile.getContent().getClass()));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testTransformWithNullList() throws TransformationException {
        CharacterImpl snakeA = new CharacterImpl("a", "a", 2);
        snakeA.setTailProtectedForGameTicks(3);

        Tile[] tiles = new WorldState(3, 3).getTiles();
        tiles[2] = new Tile(snakeA);
        tiles[7] = new Tile(new Bomb());
        tiles[8] = new Tile(new Obstacle());

        WorldState worldState = new WorldState(3, 3, tiles);

        Class[] beforeClasses = {Empty.class, Bomb.class, Obstacle.class, CharacterImpl.class};
        Class[] keepClasses = null;
        Class[] afterClasses = {Empty.class};

        for (Tile tile : worldState.getTiles()) {
            assertTrue(ArrayUtils.contains(beforeClasses, tile.getContent().getClass()));
        }

        KeepOnlyObjectsOfType keepOnlyObjectsOfType = new KeepOnlyObjectsOfType(keepClasses);
        WorldState updatedWorldState = keepOnlyObjectsOfType.transform(worldState);
        for (Tile tile : updatedWorldState.getTiles()) {
            assertTrue(ArrayUtils.contains(afterClasses, tile.getContent().getClass()));
        }
    }
}
