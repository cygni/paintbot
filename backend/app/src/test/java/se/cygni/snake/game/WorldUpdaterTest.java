package se.cygni.snake.game;

import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import se.cygni.game.WorldState;
import se.cygni.game.enums.Action;
import se.cygni.game.testutil.SnakeTestUtil;
import se.cygni.game.worldobject.Bomb;
import se.cygni.game.worldobject.Character;
import se.cygni.game.worldobject.CharacterImpl;
import se.cygni.game.worldobject.Obstacle;
import se.cygni.snake.api.model.PointReason;
import se.cygni.snake.player.RemotePlayer;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WorldUpdaterTest {

    Game game;
    PlayerManager playerManager;
    EventBus globalEventBus;
    GameFeatures gameFeatures;

    @Before
    public void setup() {
        globalEventBus = new EventBus("globaleventbus");
        gameFeatures = new GameFeatures();
        gameFeatures.setHeadToTailConsumes(true);

        playerManager = mock(PlayerManager.class);
        game = mock(Game.class);
        when(game.getGlobalEventBus()).thenReturn(globalEventBus);
        when(game.getGameFeatures()).thenReturn(gameFeatures);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(game.getGameId()).thenReturn("mock-game-id");
    }


    @Test
    public void testSimpleMove() throws Exception {
        WorldState ws = new WorldState(10, 10);

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 22, 32);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 42, 43);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.UP);
                put(p2.getPlayerId(), Action.DOWN);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(52, transformedWorld.getCharacterAtPosition(52).getPosition());
        assertEquals(p2.getPlayerId(), transformedWorld.getCharacterAtPosition(52).getPlayerId());
        assertArrayEquals(new int[] {52, 42}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(52)));

        assertEquals(12, transformedWorld.getCharacterAtPosition(12).getPosition());
        assertEquals(p1.getPlayerId(), transformedWorld.getCharacterAtPosition(12).getPlayerId());
        assertArrayEquals(new int[] {12, 22}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(12)));
    }


    @Test
    public void testCollisionWithWallMove() throws Exception {
        WorldState ws = new WorldState(10, 10);

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 2, 12);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 42, 43);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.UP);
                put(p2.getPlayerId(), Action.DOWN);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(52, transformedWorld.getCharacterAtPosition(52).getPosition());
        assertArrayEquals(new int[] {52, 42}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(52)));

        assertEquals(1, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);
        verify(p1).stunned(5);
    }

    @Test
    public void testCollisionWithObstacleMove() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 15, 16);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 42, 43);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.LEFT);
                put(p2.getPlayerId(), Action.DOWN);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(52, transformedWorld.getCharacterAtPosition(52).getPosition());
        assertArrayEquals(new int[] {52, 42}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(52)));

        assertEquals(1, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p1).stunned(5);
    }

    @Test
    public void testCollisionWithFoodMove() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();
        Bomb bomb = new Bomb();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 15, 16);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 42, 43);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 72);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, bomb, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.LEFT);
                put(p2.getPlayerId(), Action.DOWN);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(52, transformedWorld.getCharacterAtPosition(52).getPosition());
        assertEquals(p2.getPlayerId(), transformedWorld.getCharacterAtPosition(52).getPlayerId());
        assertArrayEquals(new int[] {52, 42}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(52)));

        assertEquals(14, transformedWorld.getCharacterAtPosition(14).getPosition());
        assertEquals(p1.getPlayerId(), transformedWorld.getCharacterAtPosition(14).getPlayerId());
        assertArrayEquals(new int[] {14, 15, 16}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(14)));

        assertEquals(2, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        assertEquals(obstacle, transformedWorld.getTile(72).getContent());
        verify(p1).addPoints(PointReason.FOOD, gameFeatures.getPointsPerFood());
    }

    @Test
    public void testCollisionWithSnakePartMove() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 32, 33);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 42, 43, 44);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.DOWN);
                put(p2.getPlayerId(), Action.LEFT);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(41, transformedWorld.getCharacterAtPosition(41).getPosition());
        assertEquals(p2.getPlayerId(), transformedWorld.getCharacterAtPosition(41).getPlayerId());
        assertArrayEquals(new int[] {41, 42, 43}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(41)));

        assertEquals(1, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p1).stunned(5);
        verify(p2).addPoints(PointReason.CAUSED_SNAKE_DEATH, gameFeatures.getPointsPerCausedStun());
    }

    @Test
    public void testCollisionTwoSnakeHeadsMove() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");
        RemotePlayer p3 = createPlayer("player3");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 73, 72);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 75, 76);
        Character[] parts3 = SnakeTestUtil.createSnake(p3.getName(), p3.getPlayerId(), 23, 24);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.addSnake(ws, parts3);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.RIGHT);
                put(p2.getPlayerId(), Action.LEFT);
                put(p3.getPlayerId(), Action.LEFT);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(22, transformedWorld.getCharacterAtPosition(22).getPosition());
        assertEquals(p3.getPlayerId(), transformedWorld.getCharacterAtPosition(22).getPlayerId());
        assertArrayEquals(new int[] {22, 23}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(22)));

        assertEquals(1, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p1).stunned(5);
        verify(p2).stunned(5);
    }

    @Test
    public void testCollisionHeadToTailMove() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");
        RemotePlayer p3 = createPlayer("player3");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 34, 35);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 33, 43);
        Character[] parts3 = SnakeTestUtil.createSnake(p3.getName(), p3.getPlayerId(), 82, 81);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.addSnake(ws, parts3);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.LEFT);
                put(p2.getPlayerId(), Action.UP);
                put(p3.getPlayerId(), Action.RIGHT);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(33, transformedWorld.getCharacterAtPosition(33).getPosition());
        assertEquals(p1.getPlayerId(), transformedWorld.getCharacterAtPosition(33).getPlayerId());
        assertArrayEquals(new int[] {33,34}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(33)));

        assertEquals(23, transformedWorld.getCharacterAtPosition(23).getPosition());
        assertEquals(p2.getPlayerId(), transformedWorld.getCharacterAtPosition(23).getPlayerId());
        assertArrayEquals(new int[] {23}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(23)));

        assertEquals(83, transformedWorld.getCharacterAtPosition(83).getPosition());
        assertEquals(p3.getPlayerId(), transformedWorld.getCharacterAtPosition(83).getPlayerId());
        assertArrayEquals(new int[] {83, 82}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(83)));

        assertEquals(3, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p1).addPoints(PointReason.NIBBLE, gameFeatures.getPointsPerNibble());
    }

    @Test
    public void testCollisionTwoSnakeHeadsPassingMove() throws Exception {
        gameFeatures.setHeadToTailConsumes(false);
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");
        RemotePlayer p3 = createPlayer("player3");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 34, 33);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 35, 36);
        Character[] parts3 = SnakeTestUtil.createSnake(p3.getName(), p3.getPlayerId(), 23, 24);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.addSnake(ws, parts3);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.RIGHT);
                put(p2.getPlayerId(), Action.LEFT);
                put(p3.getPlayerId(), Action.LEFT);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(22, transformedWorld.getCharacterAtPosition(22).getPosition());
        assertEquals(p3.getPlayerId(), transformedWorld.getCharacterAtPosition(22).getPlayerId());
        assertArrayEquals(new int[] {22, 23}, transformedWorld.getCharacterPosition(transformedWorld.getCharacterAtPosition(22)));

        assertEquals(1, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p1).stunned(5);
        verify(p2).stunned(5);
    }

    @Test
    public void testCollisionThreeHeads() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");
        RemotePlayer p3 = createPlayer("player3");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 34, 33);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 36, 37);
        Character[] parts3 = SnakeTestUtil.createSnake(p3.getName(), p3.getPlayerId(), 45, 55);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.addSnake(ws, parts3);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.RIGHT);
                put(p2.getPlayerId(), Action.LEFT);
                put(p3.getPlayerId(), Action.UP);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(0, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p1).stunned(5);
        verify(p2).stunned(5);
        verify(p3).stunned(5);
    }

    @Test
    public void testCollisionHeadToHead() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 34, 33);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 36, 37);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.RIGHT);
                put(p2.getPlayerId(), Action.LEFT);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(0, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p1).stunned(5);
        verify(p2).stunned(5);
    }

    @Test
    public void testCollisionPassThrough() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 34, 33, 32);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 35, 36, 37);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.RIGHT);
                put(p2.getPlayerId(), Action.LEFT);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(0, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p1).stunned(5);
        verify(p2).stunned(5);
    }

    @Test
    public void testCollisionTwoSnakeHeadsPassingLandingOnTail() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 34, 33);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 35, 36);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.RIGHT);
                put(p2.getPlayerId(), Action.LEFT);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(0, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p1).stunned(5);
        verify(p2).stunned(5);
    }

    @Test
    public void testEatEachOthersTail() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 33, 43, 53);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 44, 34, 24);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.RIGHT);
                put(p2.getPlayerId(), Action.LEFT);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(2, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);

        CharacterImpl head1 = transformedWorld.getCharacterById(p1.getPlayerId());
        assertEquals(2, transformedWorld.getCharacterPosition(head1).length);

        CharacterImpl head2 = transformedWorld.getCharacterById(p2.getPlayerId());
        assertEquals(2, transformedWorld.getCharacterPosition(head2).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p1).addPoints(
                PointReason.NIBBLE,
                gameFeatures.getPointsPerNibble()
        );
        verify(p2).addPoints(
                PointReason.NIBBLE,
                gameFeatures.getPointsPerNibble()
        );
    }

    @Test
    public void testOneEatsTailOtherCollides() throws Exception {
        WorldState ws = new WorldState(10, 10);
        Obstacle obstacle = new Obstacle();

        RemotePlayer p1 = createPlayer("player1");
        RemotePlayer p2 = createPlayer("player2");

        Character[] parts1 = SnakeTestUtil.createSnake(p1.getName(), p1.getPlayerId(), 63, 73, 83, 93);
        Character[] parts2 = SnakeTestUtil.createSnake(p2.getName(), p2.getPlayerId(), 74, 64, 54);

        ws = SnakeTestUtil.addSnake(ws, parts1);
        ws = SnakeTestUtil.addSnake(ws, parts2);
        ws = SnakeTestUtil.replaceWorldObjectAt(ws, obstacle, 14);

        Map<String, Action> snakeDirections = new HashMap<String, Action>() {
            {
                put(p1.getPlayerId(), Action.RIGHT);
                put(p2.getPlayerId(), Action.LEFT);
            }
        };

        WorldUpdater transformer = new WorldUpdater(game.getGameFeatures(), game.getPlayerManager(), game.getGameId(), game.getGlobalEventBus());
        WorldState transformedWorld = transformer.update(snakeDirections, gameFeatures, ws, false, 5);

        assertEquals(1, transformedWorld.listPositionsWithContentOf(CharacterImpl.class).length);
        CharacterImpl head1 = transformedWorld.getCharacterById(p1.getPlayerId());
        assertEquals(4, transformedWorld.getCharacterPosition(head1).length);

        assertEquals(obstacle, transformedWorld.getTile(14).getContent());
        verify(p2).stunned(5);
        verify(p1).addPoints(
                PointReason.NIBBLE,
                gameFeatures.getPointsPerNibble()
        );
    }

    private RemotePlayer createPlayer(String name) {
        RemotePlayer mockPlayer = mock(RemotePlayer.class);
        when(mockPlayer.getName()).thenReturn(name);
        when(mockPlayer.getPlayerId()).thenReturn("id-" + name);

        when(playerManager.getPlayer("id-" + name)).thenReturn(mockPlayer);

        return mockPlayer;
    }
}
