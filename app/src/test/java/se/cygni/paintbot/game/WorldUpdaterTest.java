package se.cygni.paintbot.game;

import com.google.common.eventbus.EventBus;
import org.junit.Test;
import se.cygni.game.Coordinate;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.enums.Action;
import se.cygni.game.exception.TransformationException;
import se.cygni.game.worldobject.CharacterImpl;
import se.cygni.game.worldobject.Empty;
import se.cygni.game.worldobject.Obstacle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class WorldUpdaterTest {


    /**
     * Two bots moving in opposite direction, no space in between
     * A -> <- B
     */
    @Test
    public void immediateFrontalCollision() throws TransformationException {
        int height = 5;
        int width = 5;
        WorldState ws = createEmptyWorld(height, width);

        int bot1Pos = ws.translateCoordinate(new Coordinate(1, 1));
        int bot2Pos = ws.translateCoordinate(new Coordinate(2, 1));
        Tile[] tiles = ws.getTiles();
        tiles[bot1Pos] = new Tile(new CharacterImpl("A", "A", bot1Pos), "A");
        tiles[bot2Pos] = new Tile(new CharacterImpl("B", "B", bot2Pos), "B");

        WorldState start = ws.withTiles(tiles);
        GameFeatures gameFeatures = new GameFeatures();
        WorldUpdater updater = createUpdater(gameFeatures, "immediateFrontalCollision");


        Map<String, Action> actions = new HashMap<>();
        actions.put("A", Action.RIGHT);
        actions.put("B", Action.LEFT);

        WorldState nextState = updater.update(
                actions,
                gameFeatures,
                start,
                1L
        );

        assertThat(getCoordinateOfBot("A", nextState)).isEqualTo(new Coordinate(1, 1));
        assertThat(nextState.getCharacterById("A").getPosition()).isEqualTo(bot1Pos);
        assertThat(nextState.getCharacterById("A").getIsStunnedForTicks()).isPositive();
        assertThat(getCoordinateOfBot("B", nextState)).isEqualTo(new Coordinate(2, 1));
        assertThat(nextState.getCharacterById("B").getPosition()).isEqualTo(bot2Pos);
        assertThat(nextState.getCharacterById("B").getIsStunnedForTicks()).isPositive();

        Tile[] nextStateTiles = nextState.getTiles();
        for (int i = 0; i < nextStateTiles.length; i++) {
            Tile t = nextStateTiles[i];
            if (i != bot1Pos && i != bot2Pos) {
                assertThat(t.getContent()).isInstanceOf(Empty.class);
            }
        }
    }


    /**
     * Two bots moving in opposite direction, empty space between
     * A -> E <- B
     */
    @Test
    public void frontalCollisionWithSpace() throws TransformationException {
        int height = 5;
        int width = 5;
        WorldState ws = createEmptyWorld(height, width);

        Coordinate bot1Coord = new Coordinate(1, 1);
        Coordinate bot2Coord = new Coordinate(3, 1);
        int bot1Pos = ws.translateCoordinate(bot1Coord);
        int bot2Pos = ws.translateCoordinate(bot2Coord);
        Tile[] tiles = ws.getTiles();
        tiles[bot1Pos] = new Tile(new CharacterImpl("A", "A", bot1Pos), "A");
        tiles[bot2Pos] = new Tile(new CharacterImpl("B", "B", bot2Pos), "B");

        WorldState start = ws.withTiles(tiles);
        GameFeatures gameFeatures = new GameFeatures();
        WorldUpdater updater = createUpdater(gameFeatures, "frontalCollisionWithSpace");


        Map<String, Action> actions = new HashMap<>();
        actions.put("A", Action.RIGHT);
        actions.put("B", Action.LEFT);

        WorldState nextState = updater.update(
                actions,
                gameFeatures,
                start,
                1L
        );

        assertThat(getCoordinateOfBot("A", nextState)).isEqualTo(bot1Coord);
        assertThat(nextState.getCharacterById("A").getPosition()).isEqualTo(bot1Pos);
        assertThat(nextState.getCharacterById("A").getIsStunnedForTicks()).isPositive();
        assertThat(getCoordinateOfBot("B", nextState)).isEqualTo(bot2Coord);
        assertThat(nextState.getCharacterById("B").getPosition()).isEqualTo(bot2Pos);
        assertThat(nextState.getCharacterById("B").getIsStunnedForTicks()).isPositive();

        Tile[] nextStateTiles = nextState.getTiles();
        for (int i = 0; i < nextStateTiles.length; i++) {
            Tile t = nextStateTiles[i];
            if (i != bot1Pos && i != bot2Pos) {
                assertThat(t.getContent()).isInstanceOf(Empty.class);
            }
        }
    }


    /**
     * Two bots moving, one of them takes a space previously occupied by first bot
     * Before: A -> B -> E
     * After:  E -> A -> B
     */
    @Test
    public void canTakeSpacePreviouslyOccupied() throws TransformationException {
        int height = 5;
        int width = 5;
        WorldState ws = createEmptyWorld(height, width);

        Coordinate bot1Coord = new Coordinate(1, 1);
        Coordinate bot2Coord = new Coordinate(2, 1);
        int bot1StartPos = ws.translateCoordinate(bot1Coord);
        int bot2StartPos = ws.translateCoordinate(bot2Coord);
        Tile[] tiles = ws.getTiles();
        tiles[bot1StartPos] = new Tile(new CharacterImpl("A", "A", bot1StartPos), "A");
        tiles[bot2StartPos] = new Tile(new CharacterImpl("B", "B", bot2StartPos), "B");

        WorldState start = ws.withTiles(tiles);
        GameFeatures gameFeatures = new GameFeatures();
        WorldUpdater updater = createUpdater(gameFeatures, "frontalCollisionWithSpace");


        Map<String, Action> actions = new HashMap<>();
        actions.put("A", Action.RIGHT);
        actions.put("B", Action.RIGHT);

        WorldState nextState = updater.update(
                actions,
                gameFeatures,
                start,
                1L
        );

        assertThat(getCoordinateOfBot("A", nextState)).isEqualTo(new Coordinate(2, 1));
        assertThat(nextState.getTile(nextState.getPositionOfPlayer("A")).getOwnerID()).isEqualTo("A");
        assertThat(nextState.getTile(bot1StartPos).getOwnerID()).isEqualTo("A");
        assertThat(nextState.getCharacterById("A").getIsStunnedForTicks()).isZero();


        assertThat(getCoordinateOfBot("B", nextState)).isEqualTo(new Coordinate(3, 1));
        assertThat(nextState.getCharacterById("B").getIsStunnedForTicks()).isZero();
        assertThat(nextState.getTile(nextState.getPositionOfPlayer("B")).getOwnerID()).isEqualTo("B");
        assertThat(nextState.getTile(bot2StartPos).getOwnerID()).isEqualTo("A");

        Tile[] nextStateTiles = nextState.getTiles();
        for (int i = 0; i < nextStateTiles.length; i++) {
            Tile t = nextStateTiles[i];
            if (i != nextState.getCharacterById("A").getPosition() &&
                    i != nextState.getCharacterById("B").getPosition()) {
                assertThat(t.getContent()).isInstanceOf(Empty.class);
            }
        }
    }


    /**
     * Dancing in a circle
     * <p>
     * Before:
     * E E E E
     * E A B E
     * E C D E
     * E E E E
     * <p>
     * After:
     * E E E E
     * E C A E
     * E D B E
     * E E E E
     */
    @Test
    public void dancingInACircle() throws TransformationException {
        int height = 5;
        int width = 5;
        WorldState ws = createEmptyWorld(height, width);

        Coordinate bot1Coord = new Coordinate(1, 1);
        Coordinate bot2Coord = new Coordinate(2, 1);
        Coordinate bot3Coord = new Coordinate(1, 2);
        Coordinate bot4Coord = new Coordinate(2, 2);
        int bot1StartPos = ws.translateCoordinate(bot1Coord);
        int bot2StartPos = ws.translateCoordinate(bot2Coord);
        int bot3StartPos = ws.translateCoordinate(bot3Coord);
        int bot4StartPos = ws.translateCoordinate(bot4Coord);

        Tile[] tiles = ws.getTiles();
        tiles[bot1StartPos] = new Tile(new CharacterImpl("A", "A", bot1StartPos), "A");
        tiles[bot2StartPos] = new Tile(new CharacterImpl("B", "B", bot2StartPos), "B");
        tiles[bot3StartPos] = new Tile(new CharacterImpl("C", "C", bot3StartPos), "C");
        tiles[bot4StartPos] = new Tile(new CharacterImpl("D", "D", bot4StartPos), "D");

        WorldState start = ws.withTiles(tiles);
        GameFeatures gameFeatures = new GameFeatures();
        WorldUpdater updater = createUpdater(gameFeatures, "dancingInACircle");


        Map<String, Action> actions = new HashMap<>();
        actions.put("A", Action.RIGHT);
        actions.put("B", Action.DOWN);
        actions.put("C", Action.UP);
        actions.put("D", Action.LEFT);

        WorldState nextState = updater.update(
                actions,
                gameFeatures,
                start,
                1L
        );

        assertBotAtPos("A", new Coordinate(2, 1), 0, 0, nextState);
        assertBotAtPos("B", new Coordinate(2, 2), 0, 0, nextState);
        assertBotAtPos("C", new Coordinate(1, 1), 0, 0, nextState);
        assertBotAtPos("D", new Coordinate(1, 2), 0, 0, nextState);

        Tile[] nextStateTiles = nextState.getTiles();
        for (int i = 0; i < nextStateTiles.length; i++) {
            Tile t = nextStateTiles[i];
            final int pos = i;
            if (Stream.of("A", "B", "C", "D")
                    .noneMatch(botId -> nextState.getCharacterById(botId).getPosition() == pos)
            ) {
                assertThat(t.getContent()).isInstanceOf(Empty.class);
            }
        }
    }


    /**
     * Chain collision
     * <p>
     * Before:
     * E E E E E
     * E A B C D
     * E E E E E
     * E E E E E
     * <p>
     * Everyone is moving to towards B, B is moving right
     * <p>
     * After:
     * E E E E E
     * E A B C D
     * E E E E E
     * E E E E E
     */
    @Test
    public void chainCollision() throws TransformationException {
        int height = 5;
        int width = 5;
        WorldState ws = createEmptyWorld(height, width);

        Coordinate bot1Coord = new Coordinate(1, 1);
        Coordinate bot2Coord = new Coordinate(2, 1);
        Coordinate bot3Coord = new Coordinate(3, 1);
        Coordinate bot4Coord = new Coordinate(4, 1);
        int bot1StartPos = ws.translateCoordinate(bot1Coord);
        int bot2StartPos = ws.translateCoordinate(bot2Coord);
        int bot3StartPos = ws.translateCoordinate(bot3Coord);
        int bot4StartPos = ws.translateCoordinate(bot4Coord);

        Tile[] tiles = ws.getTiles();
        tiles[bot1StartPos] = new Tile(new CharacterImpl("A", "A", bot1StartPos), "A");
        tiles[bot2StartPos] = new Tile(new CharacterImpl("B", "B", bot2StartPos), "B");
        tiles[bot3StartPos] = new Tile(new CharacterImpl("C", "C", bot3StartPos), "C");
        tiles[bot4StartPos] = new Tile(new CharacterImpl("D", "D", bot4StartPos), "D");

        WorldState start = ws.withTiles(tiles);
        GameFeatures gameFeatures = new GameFeatures();
        WorldUpdater updater = createUpdater(gameFeatures, "chainCollision");


        Map<String, Action> actions = new HashMap<>();
        actions.put("A", Action.RIGHT);
        actions.put("B", Action.RIGHT);
        actions.put("C", Action.LEFT);
        actions.put("D", Action.LEFT);

        WorldState nextState = updater.update(
                actions,
                gameFeatures,
                start,
                1L
        );

        assertBotAtPos("A", bot1Coord, gameFeatures.getMinNoOfTicksStunned(), gameFeatures.getMaxNoOfTicksStunned(), nextState);
        assertBotAtPos("B", bot2Coord, gameFeatures.getMinNoOfTicksStunned(), gameFeatures.getMaxNoOfTicksStunned(), nextState);
        assertBotAtPos("C", bot3Coord, gameFeatures.getMinNoOfTicksStunned(), gameFeatures.getMaxNoOfTicksStunned(), nextState);
        assertBotAtPos("D", bot4Coord, gameFeatures.getMinNoOfTicksStunned(), gameFeatures.getMaxNoOfTicksStunned(), nextState);

        Tile[] nextStateTiles = nextState.getTiles();
        for (int i = 0; i < nextStateTiles.length; i++) {
            Tile t = nextStateTiles[i];
            final int pos = i;
            if (Stream.of("A", "B", "C", "D")
                    .noneMatch(botId -> nextState.getCharacterById(botId).getPosition() == pos)
            ) {
                assertThat(t.getContent()).isInstanceOf(Empty.class);
            }
        }
    }

    @Test
    public void obstacleCollision() throws TransformationException {
        int height = 5;
        int width = 5;
        WorldState ws = createEmptyWorld(height, width);

        Coordinate bot1Coord = new Coordinate(1, 1);
        Coordinate obstacleCoord = new Coordinate(2, 1);
        int bot1Pos = ws.translateCoordinate(bot1Coord);
        int obstaclePos = ws.translateCoordinate(obstacleCoord);
        Tile[] tiles = ws.getTiles();
        tiles[bot1Pos] = new Tile(new CharacterImpl("A", "A", bot1Pos), "A");
        tiles[obstaclePos] = new Tile(new Obstacle());

        WorldState start = ws.withTiles(tiles);
        GameFeatures gameFeatures = new GameFeatures();
        WorldUpdater updater = createUpdater(gameFeatures, "obstacleCollision");

        Map<String, Action> actions = new HashMap<>();
        actions.put("A", Action.RIGHT);

        WorldState nextState = updater.update(
                actions,
                gameFeatures,
                start,
                1L
        );

        assertBotAtPos("A", bot1Coord, gameFeatures.getMinNoOfTicksStunned(), gameFeatures.getMaxNoOfTicksStunned(), nextState);

        Tile[] nextStateTiles = nextState.getTiles();
        assertThat(nextStateTiles[obstaclePos].getContent()).isInstanceOf(Obstacle.class);
        for (int i = 0; i < nextStateTiles.length; i++) {
            Tile t = nextStateTiles[i];
            if (i != bot1Pos && i != obstaclePos) {
                assertThat(t.getContent()).isInstanceOf(Empty.class);
            }
        }
    }

    /**
     * Bot A uses explosion, tiles within explosion range of the bot are then owned by it
     */
    @Test
    public void basicExplosion() throws TransformationException {
        int height = 20;
        int width = 20;

        WorldState ws = createEmptyWorld(height, width);
        Coordinate bot1Coord = new Coordinate(10, 10);
        Coordinate obstacleCoord = new Coordinate(12, 10);
        int bot1Pos = ws.translateCoordinate(bot1Coord);
        int obstaclePos = ws.translateCoordinate(obstacleCoord);
        Tile[] tiles = ws.getTiles();
        CharacterImpl character = new CharacterImpl("A", "A", bot1Pos);
        character.setCarryingPowerUp(true);
        tiles[bot1Pos] = new Tile(character, "A");
        tiles[obstaclePos] = new Tile(new Obstacle());

        WorldState start = ws.withTiles(tiles);
        GameFeatures gameFeatures = new GameFeatures();
        WorldUpdater updater = createUpdater(gameFeatures, "basicExplosion");

        Map<String, Action> actions = new HashMap<>();
        actions.put("A", Action.EXPLODE);

        WorldState nextState = updater.update(
                actions,
                gameFeatures,
                start,
                1L
        );

        assertBotAtPos("A", bot1Coord, 0, 0, nextState);
        assertThat(nextState.getCharacterById("A").isCarryingPowerUp()).isFalse();

        Tile[] nextStateTiles = nextState.getTiles();
        assertThat(nextStateTiles[obstaclePos].getContent()).isInstanceOf(Obstacle.class);
        for (int i = 0; i < nextStateTiles.length; i++) {
            Tile t = nextStateTiles[i];
            if (i != bot1Pos && i != obstaclePos) {
                assertThat(t.getContent()).isInstanceOf(Empty.class);

                if (manhattanDistance(nextState.translatePosition(i), nextState
                        .translatePosition(bot1Pos)) <= gameFeatures.getExplosionRange()) {
                    assertThat(t.getOwnerID()).isEqualTo("A");
                }
            }
        }
    }


    /**
     * Bots A, B and C uses explosion, tiles within explosion range of the bots are then owned by it
     * Tiles hit by multiple explosions are decided randomly who get's ownership.
     * Bots hit gets stunned.
     */
    @Test
    public void crossingExplosion() throws TransformationException {
        int height = 20;
        int width = 20;

        WorldState ws = createEmptyWorld(height, width);
        Coordinate bot1Coord = new Coordinate(10, 10);
        Coordinate bot2Coord = new Coordinate(14, 10);
        Coordinate bot3Coord = new Coordinate(8, 9);
        Coordinate obstacleCoord = new Coordinate(12, 10);
        int bot1Pos = ws.translateCoordinate(bot1Coord);
        int bot2Pos = ws.translateCoordinate(bot2Coord);
        int bot3Pos = ws.translateCoordinate(bot3Coord);
        int obstaclePos = ws.translateCoordinate(obstacleCoord);
        Tile[] tiles = ws.getTiles();

        CharacterImpl character1 = new CharacterImpl("A", "A", bot1Pos);
        character1.setCarryingPowerUp(true);
        tiles[bot1Pos] = new Tile(character1, "A");

        CharacterImpl character2 = new CharacterImpl("B", "B", bot2Pos);
        character2.setCarryingPowerUp(true);
        tiles[bot2Pos] = new Tile(character2, "B");


        CharacterImpl character3 = new CharacterImpl("C", "C", bot3Pos);
        character3.setCarryingPowerUp(true);
        tiles[bot3Pos] = new Tile(character3, "C");

        tiles[obstaclePos] = new Tile(new Obstacle());

        WorldState start = ws.withTiles(tiles);
        GameFeatures gameFeatures = new GameFeatures();
        WorldUpdater updater = createUpdater(gameFeatures, "crossingExplosion");

        Map<String, Action> actions = new HashMap<>();
        actions.put("A", Action.EXPLODE);
        actions.put("B", Action.EXPLODE);
        actions.put("C", Action.EXPLODE);

        WorldState nextState = updater.update(
                actions,
                gameFeatures,
                start,
                1L
        );

        assertBotAtPos("A", bot1Coord, gameFeatures.getMinNoOfTicksStunned(), gameFeatures.getMaxNoOfTicksStunned(), nextState);
        assertThat(nextState.getCharacterById("A").isCarryingPowerUp()).isFalse();

        assertBotAtPos("B", bot2Coord, gameFeatures.getMinNoOfTicksStunned(), gameFeatures.getMaxNoOfTicksStunned(), nextState);
        assertThat(nextState.getCharacterById("B").isCarryingPowerUp()).isFalse();

        assertBotAtPos("C", bot3Coord, gameFeatures.getMinNoOfTicksStunned(), gameFeatures.getMaxNoOfTicksStunned(), nextState);
        assertThat(nextState.getCharacterById("C").isCarryingPowerUp()).isFalse();

        Tile[] nextStateTiles = nextState.getTiles();
        assertThat(nextStateTiles[obstaclePos].getContent()).isInstanceOf(Obstacle.class);
        for (int i = 0; i < nextStateTiles.length; i++) {
            Tile t = nextStateTiles[i];
            final int pos = i;
            if (Stream.of(bot1Pos, bot2Pos, bot3Pos, obstaclePos).noneMatch(p -> p != pos)) {

                assertThat(t.getContent()).isInstanceOf(Empty.class);
                boolean aInRange = manhattanDistance(nextState.translatePosition(i), nextState
                        .translatePosition(bot1Pos)) <= gameFeatures.getExplosionRange();
                boolean bInRange = manhattanDistance(nextState.translatePosition(i), nextState
                        .translatePosition(bot2Pos)) <= gameFeatures.getExplosionRange();
                boolean cInRange = manhattanDistance(nextState.translatePosition(i), nextState
                        .translatePosition(bot3Pos)) <= gameFeatures.getExplosionRange();

                if (aInRange || bInRange || cInRange) {

                    Set<String> inRange = new HashSet<>();
                    if (aInRange) {
                        inRange.add("A");
                    }
                    if (bInRange) {
                        inRange.add("B");
                    }
                    if (cInRange) {
                        inRange.add("C");
                    }

                    assertThat(t.getOwnerID()).isIn(inRange);
                }
            }
        }
    }

    private int manhattanDistance(Coordinate pos1, Coordinate pos2) {
        return Math.abs(pos1.getX() - pos2.getX()) + Math.abs(pos1.getY() - pos2.getY());
    }

    private void assertBotAtPos(String botId, Coordinate coordinate, int minStunnedForTicks, int maxStunnedForTicks, WorldState nextState) {
        assertThat(getCoordinateOfBot(botId, nextState)).isEqualTo(coordinate);
        assertThat(nextState.getTile(nextState.getPositionOfPlayer(botId)).getOwnerID()).isEqualTo(botId);
        assertThat(nextState.getCharacterById(botId).getIsStunnedForTicks()).isGreaterThanOrEqualTo(minStunnedForTicks);
        assertThat(nextState.getCharacterById(botId).getIsStunnedForTicks()).isLessThanOrEqualTo(maxStunnedForTicks);
    }

    private Coordinate getCoordinateOfBot(String botId, WorldState nextState) {
        return nextState.translatePosition(nextState.getCharacterById(botId).getPosition());
    }

    private WorldUpdater createUpdater(GameFeatures gameFeatures, String identifier) {
        PlayerManager playerManager = new PlayerManager();
        EventBus eventBus = new EventBus(identifier + "EventBus");

        return new WorldUpdater(gameFeatures,
                playerManager,
                identifier,
                eventBus
        );
    }

    private WorldState createEmptyWorld(int height, int width) {
        Tile[] tiles = new Tile[height * width];
        for (int i = 0; i < height * width; i++) {
            tiles[i] = new Tile();
        }

        return new WorldState(width, height, tiles);
    }
}