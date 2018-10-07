package se.cygni.snake.client;

import se.cygni.snake.api.model.*;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;

public class MapUtil {

    private final Map map;
    private final int mapSize;
    private final String playerId;
    private final java.util.Map<String, CharacterInfo> characterInfoMap;
    private final java.util.Map<String, BitSet> snakeSpread;
    private final BitSet bombs;
    private final BitSet obstacles;
    private final BitSet characters;


    public MapUtil(Map map, String playerId) {
        this.map = map;
        this.mapSize = map.getHeight() * map.getWidth();

        this.playerId = playerId;
        characterInfoMap = new HashMap<>();
        snakeSpread = new HashMap<>();

        int mapLength = map.getHeight() * map.getWidth();
        bombs = new BitSet(mapLength);
        obstacles = new BitSet(mapLength);
        characters = new BitSet(mapLength);

        populateCharacterInfo();
        populateStaticTileBits();
    }

    public boolean canIMoveInDirection(CharacterAction direction) {
        try {
            MapCoordinate myPos = getMyPosition();
            MapCoordinate myNewPos = myPos.translateByDirection(direction);

            return isTileAvailableForMovementTo(myNewPos);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns an array of coordinates painted in the provided player's colour.
     *
     * @param playerId
     * @return an array of MapCoordinate coloured by the player with matching playerId
     */
    public MapCoordinate[] getPlayerColouredPositions(String playerId) {
        return translatePositions(characterInfoMap.get(playerId).getColouredPositions());
    }


    /**
     * @return An array containing all MapCoordinates where there's Food
     */
    public MapCoordinate[] listCoordinatesContainingBombs() {
        return translatePositions(map.getBombPositions());
    }

    /**
     * @return An array containing all MapCoordinates where there's an Obstacle
     */
    public MapCoordinate[] listCoordinatesContainingObstacle() {
        return translatePositions(map.getObstaclePositions());
    }

    /**
     * @param coordinate
     * @return true if the TileContent at coordinate is Empty or contains Food
     */
    public boolean isTileAvailableForMovementTo(MapCoordinate coordinate) {
        if (isCoordinateOutOfBounds(coordinate))
            return false;

        int position = translateCoordinate(coordinate);
        return isTileAvailableForMovementTo(position);
    }

    /**
     * @param position map position
     * @return true if the TileContent at map position is Empty or contains Food
     */
    private boolean isTileAvailableForMovementTo(int position) {
        if (isPositionOutOfBounds(position))
            return false;

        return !(obstacles.get(position) || characters.get(position));
    }

    /**
     * @return The MapCoordinate of your character
     */
    public MapCoordinate getMyPosition() {
        return translatePosition(
                characterInfoMap.get(playerId).getPosition());
    }

    /**
     * @param coordinate map coordinate
     * @return whether or not it is out of bounds
     */
    public boolean isCoordinateOutOfBounds(MapCoordinate coordinate) {
        return coordinate.x < 0 || coordinate.x >= map.getWidth() || coordinate.y < 0 || coordinate.y >= map.getHeight();
    }

    /**
     * @param position map position
     * @return whether or not it is out of bounds
     */
    private boolean isPositionOutOfBounds(int position) {
        return position < 0 || position >= mapSize;
    }

    /**
     * @param position map position
     * @return the TileContent at the specified position of the flattened map.
     */
    private TileContent getTileAt(int position) {
        if (isPositionOutOfBounds(position)) {
            String errorMessage = String.format("Position [%s] is out of bounds", position);
            throw new RuntimeException(errorMessage);
        }

        if (bombs.get(position)) {
            return new MapBomb();
        }

        if (obstacles.get(position)) {
            return new MapObstacle();
        }

        if (characters.get(position)) {
            return getCharacter(position);
        }

        return new MapEmpty();
    }

    /**
     * @param coordinate
     * @return the TileContent at the specified coordinate
     */
    public TileContent getTileAt(MapCoordinate coordinate) {
        return getTileAt(translateCoordinate(coordinate));
    }

    /**
     * Converts a position in the flattened single array representation
     * of the Map to a MapCoordinate.
     *
     * @param position
     * @return
     */
    public MapCoordinate translatePosition(int position) {
        int y = position / map.getWidth();
        int x = position - y * map.getWidth();
        return new MapCoordinate(x, y);
    }

    /**
     * Converts a MapCoordinate to the same position in the flattened
     * single array representation of the Map.
     *
     * @param coordinate
     * @return
     */
    public int translateCoordinate(MapCoordinate coordinate) {
        if (isCoordinateOutOfBounds(coordinate)) {
            String errorMessage = String.format("Coordinate [%s,%s] is out of bounds", coordinate.x, coordinate.y);
            throw new RuntimeException(errorMessage);
        }

        return coordinate.x + coordinate.y * map.getWidth();
    }

    public MapCoordinate[] translatePositions(int[] positions) {
        return Arrays.stream(positions)
                .mapToObj(pos -> translatePosition(pos))
                .toArray(MapCoordinate[]::new);
    }

    public int[] translateCoordinates(MapCoordinate[] coordinates) {
        return Arrays.stream(coordinates)
                .mapToInt(coordinate -> translateCoordinate(coordinate))
                .toArray();
    }

    private TileContent getCharacter(int position) {
        String playerId = getPlayerIdAtPosition(position);
        CharacterInfo characterInfo = characterInfoMap.get(playerId);
        return new MapCharacter(characterInfo.getName(), playerId);
    }

    private String getPlayerIdAtPosition(int position) {
        for (CharacterInfo characterInfo : map.getCharacterInfos()) {
            if (snakeSpread
                    .get(characterInfo.getId())
                    .get(position)) {

                return characterInfo.getId();
            }
        }
        throw new RuntimeException("No snake at position: " + position);
    }

    private void populateCharacterInfo() {
        for (CharacterInfo characterInfo : map.getCharacterInfos()) {
            characterInfoMap.put(characterInfo.getId(), characterInfo);
            characters.set(characterInfo.getPosition());
        }
    }

    private void populateStaticTileBits() {
        for (int pos : map.getBombPositions()) {
            bombs.set(pos);
        }
        for (int pos : map.getObstaclePositions()) {
            obstacles.set(pos);
        }
    }
}
