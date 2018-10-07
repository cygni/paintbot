package se.cygni.game.transformation;

import org.junit.Test;
import se.cygni.game.WorldState;
import se.cygni.game.testutil.SnakeTestUtil;
import se.cygni.game.worldobject.Bomb;
import se.cygni.game.worldobject.Obstacle;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RemoveRandomWorldObjectTest {

    @Test
    public void testRemoveObstacle() throws Exception {
        WorldState ws = SnakeTestUtil.createWorld(Obstacle.class, 10, 10, 9, 12, 18);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, new Bomb(), 10);

        RemoveRandomWorldObject<Obstacle> removeTransformation = new RemoveRandomWorldObject<>(Obstacle.class);

        ws = removeTransformation.transform(ws);

        assertArrayEquals(new int[]{10}, ws.listBombPositions());
        assertEquals(2, ws.listObstaclePositions().length);
    }

    @Test
    public void testNothingChangesIfObjectTypeDoesntExists() throws Exception {
        WorldState ws = SnakeTestUtil.createWorld(Obstacle.class, 10, 10, 9, 12, 18);

        RemoveRandomWorldObject<Bomb> removeTransformation = new RemoveRandomWorldObject<>(Bomb.class);

        ws = removeTransformation.transform(ws);

        assertArrayEquals(new int[]{9, 12, 18}, ws.listObstaclePositions());
        assertEquals(0, ws.listBombPositions().length);
    }

    @Test
    public void testTransformWithNulls() throws Exception {
        WorldState ws = SnakeTestUtil.createWorld(Obstacle.class, 10, 10, 9, 12, 18);
        RemoveRandomWorldObject<Bomb> removeTransformation = new RemoveRandomWorldObject<>(null);
        ws = removeTransformation.transform(ws);
    }
}