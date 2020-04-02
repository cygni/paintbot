package se.cygni.paintbot.client;

import se.cygni.paintbot.api.model.CharacterAction;
import se.cygni.paintbot.api.model.CharacterInfo;
import se.cygni.paintbot.api.model.Map;
import se.cygni.paintbot.api.model.MapCharacter;
import se.cygni.paintbot.api.model.MapEmpty;
import se.cygni.paintbot.api.model.MapObstacle;
import se.cygni.paintbot.api.model.MapPowerUp;
import se.cygni.paintbot.api.model.TileContent;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Optional;

public class MapUtilityImpl implements MapUtility {

    private final Map map;
    private final int mapSize;
    private final String playerId;
    private final java.util.Map<String, CharacterInfo> characterInfoMap;
    private final BitSet powerUps;
    private final BitSet obstacles;
    private final BitSet characters;


    public MapUtilityImpl(Map map, String playerId) {
        this.map = map;
        this.mapSize = map.getHeight() * map.getWidth();

        this.playerId = playerId;
        characterInfoMap = new HashMap<>();

        int mapLength = map.getHeight() * map.getWidth();
        powerUps = new BitSet(mapLength);
        obstacles = new BitSet(mapLength);
        characters = new BitSet(mapLength);

        populateCharacterInfo();
        populateStaticTileBits();
    }

    @Override
    public boolean canIMoveInDirection(CharacterAction direction) {
        try {
            MapCoordinate myPos = getMyPosition();
            MapCoordinate myNewPos = myPos.translateByDirection(direction);

            return isTileAvailableForMovementTo(myNewPos);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public MapCoordinate[] getPlayerColouredPositions(String playerId) {
        return convertPositionsToCoordinates(characterInfoMap.get(playerId).getColouredPositions());
    }


    @Override
    public MapCoordinate[] listCoordinatesContainingPowerUps() {
        return convertPositionsToCoordinates(map.getPowerUpPositions());
    }

    @Override
    public MapCoordinate[] listCoordinatesContainingObstacle() {
        return convertPositionsToCoordinates(map.getObstaclePositions());
    }

    @Override
    public boolean isTileAvailableForMovementTo(MapCoordinate coordinate) {
        if (isCoordinateOutOfBounds(coordinate))
            return false;

        int position = convertCoordinateToPosition(coordinate);
        return isTileAvailableForMovementTo(position);
    }

    private boolean isTileAvailableForMovementTo(int position) {
        if (isPositionOutOfBounds(position))
            return false;

        return !(obstacles.get(position) || characters.get(position));
    }

    @Override
    public MapCoordinate getMyPosition() {
        return convertPositionToCoordinate(
                characterInfoMap.get(playerId).getPosition());
    }

    @Override
    public CharacterInfo getMyCharacterInfo() {
        return characterInfoMap.get(playerId);
    }

    @Override
    public Optional<CharacterInfo> getCharacterInfoOf(String playerId) {
        return Optional.ofNullable(characterInfoMap.get(playerId));
    }

    @Override
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

        if (powerUps.get(position)) {
            return new MapPowerUp();
        }

        if (obstacles.get(position)) {
            return new MapObstacle();
        }

        if (characters.get(position)) {
            return getCharacter(position);
        }

        return new MapEmpty();
    }

    @Override
    public TileContent getTileAt(MapCoordinate coordinate) {
        return getTileAt(convertCoordinateToPosition(coordinate));
    }

    @Override
    public MapCoordinate convertPositionToCoordinate(int position) {
        int y = position / map.getWidth();
        int x = position - y * map.getWidth();
        return new MapCoordinate(x, y);
    }

    @Override
    public int convertCoordinateToPosition(MapCoordinate coordinate) {
        if (isCoordinateOutOfBounds(coordinate)) {
            String errorMessage = String.format("Coordinate [%s,%s] is out of bounds", coordinate.x, coordinate.y);
            throw new RuntimeException(errorMessage);
        }

        return coordinate.x + coordinate.y * map.getWidth();
    }

    @Override
    public MapCoordinate[] convertPositionsToCoordinates(int[] positions) {
        return Arrays.stream(positions)
                .mapToObj(this::convertPositionToCoordinate)
                .toArray(MapCoordinate[]::new);
    }

    @Override
    public int[] convertCoordinatesToPositions(MapCoordinate[] coordinates) {
        return Arrays.stream(coordinates)
                .mapToInt(this::convertCoordinateToPosition)
                .toArray();
    }

    private TileContent getCharacter(int position) {
        String playerId = getPlayerIdAtPosition(position);
        CharacterInfo characterInfo = characterInfoMap.get(playerId);
        return new MapCharacter(characterInfo.getName(), playerId);
    }

    private String getPlayerIdAtPosition(int position) {
        for (CharacterInfo characterInfo : map.getCharacterInfos()) {
            if (characterInfo.getPosition() == position) {
                return characterInfo.getId();
            }
        }
        throw new RuntimeException("No paintbot at position: " + position);
    }

    private void populateCharacterInfo() {
        for (CharacterInfo characterInfo : map.getCharacterInfos()) {
            characterInfoMap.put(characterInfo.getId(), characterInfo);
            characters.set(characterInfo.getPosition());
        }
    }

    private void populateStaticTileBits() {
        for (int pos : map.getPowerUpPositions()) {
            powerUps.set(pos);
        }
        for (int pos : map.getObstaclePositions()) {
            obstacles.set(pos);
        }
    }
}
