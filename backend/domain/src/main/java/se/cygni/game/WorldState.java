package se.cygni.game;

import org.apache.commons.lang3.ArrayUtils;
import se.cygni.game.enums.Action;
import se.cygni.game.exception.OutOfBoundsException;
import se.cygni.game.worldobject.*;
import se.cygni.game.worldobject.Character;
import se.cygni.game.worldobject.CharacterImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class WorldState {

    private final int width, height;
    private final Tile[] tiles;

    public WorldState(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = createEmptyTiles();
    }

    public WorldState(int width, int height, Tile[] tiles) {
        this.width = width;
        this.height = height;
        this.tiles = ArrayUtils.clone(tiles);
    }

    public WorldState(WorldState worldState) {
        this.width = worldState.getWidth();
        this.height = worldState.getHeight();
        this.tiles = worldState.getTiles();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Coordinate translatePosition(int position) {
        int y = position / width;
        int x = position - y * width;
        return new Coordinate(x, y);
    }

    public int translateCoordinate(Coordinate coordinate) {
        return coordinate.getX() + coordinate.getY() * width;
    }

    public int getSize() {
        return width * height;
    }

    public Tile getTile(int position) {
        if (position < 0)
            throw new OutOfBoundsException("Can not get tiles at negative position");
        if (position >= getSize())
            throw new OutOfBoundsException("Can not get tiles beyond world");

        return tiles[position];
    }

    public boolean isTileEmpty(int position) {
        return isTileContentOfType(position, Empty.class);
    }

    public <T extends WorldObject> boolean isTileContentOfType(int position, Class<T> clazz) {
        return getTile(position).getContent().getClass() == clazz;
    }

    public int getPositionOfSnakeHead(String playerId) {
        int[] snakeHeadPositions = listPositionsWithContentOf(CharacterImpl.class);

        for (int position : snakeHeadPositions) {
            CharacterImpl sh = (CharacterImpl)getTile(position).getContent();
            if (sh.getPlayerId().equals(playerId))
                return position;
        }
        return -1;
    }

    public int getPositionOfPlayer(String playerId) {
        return getCharacterById(playerId).getPosition();
    }

    public CharacterImpl getCharacterById(String playerId) {
        int[] characterPositions = listPositionsWithContentOf(CharacterImpl.class);
        for (int pos : characterPositions) {
            CharacterImpl character = (CharacterImpl)tiles[pos].getContent();
            if (character.getPlayerId().equals(playerId)) {
                return character;
            }
        }
        throw new IllegalArgumentException("Could not find CharacterImpl with playerId " + playerId);
    }

    public CharacterImpl getCharacterAtPosition(int position) {
        if (! (getTile(position).getContent() instanceof Character)) {
            throw new RuntimeException("Tile at position " + position + " didn't contain a Character");
        }

        int[] characterPositions = listPositionsWithContentOf(CharacterImpl.class);
        for (int characterPos : characterPositions) {
            if(characterPos == position) {
                return (CharacterImpl)getTile(characterPos).getContent();
            }
        }

        throw new IllegalStateException("Found Character without position");
    }

    public List<String> listCharacterIds() {

        return Arrays.stream(tiles).filter(tile -> tile.getContent() instanceof CharacterImpl)
                .map(tile1 -> {
                    return ((CharacterImpl)tile1.getContent()).getPlayerId();
                }).collect(Collectors.toList());
    }

    /**
     *
     * @param character
     * @return a position that this character occupies
     */
    public int getCharacterPosition(CharacterImpl character) {
        return character.getPosition();
    }

    public int getPositionForAdjacent(int position, Action action) {
        if (!hasAdjacentTile(position, action))
            throw new OutOfBoundsException("Tile " + action + " from position " + position + " is out of bounds");

        switch (action) {
            case DOWN:  return position + width;
            case UP:    return position - width;
            case RIGHT: return position + 1;
            case LEFT:  return position - 1;
            default:    throw new RuntimeException("Invalid action");
        }
    }

    /**
     * Positions that are adjacent to a CharacterImpl
     *
     * @return
     */
    public int[] listPositionsAdjacentToCharacters() {
        int[] characterPositions = listPositionsWithContentOf(CharacterImpl.class);

        return IntStream.of(characterPositions).flatMap(pos ->
            IntStream.of(
                    listAdjacentTiles(pos)
            )
        ).toArray();
    }

    public int[] listAdjacentTiles(int position) {
        return Stream.of(Action.values())
                .mapToInt(direction -> {
                    if (hasAdjacentTile(position, direction))
                        return getPositionForAdjacent(position, direction);
                    return -1;
                })
                .filter(p -> p >= 0)
                .toArray();
    }

    /**
     *
     * @param position
     * @param action
     * @return True if the adjacent is within bounds (i.e. not a wall)
     */
    public boolean hasAdjacentTile(int position, Action action) {
        switch (action) {
            case UP   : return (position-width >= 0);
            case DOWN : return (position+width < getSize());
            case LEFT : return (position % width != 0);
            case RIGHT: return ((position+1) % width != 0);
            default   : return false;
        }
    }

    public <T extends WorldObject> int[] listPositionsWithContentOf(Class<T> clazz) {
        return IntStream.range(0, getSize()).filter( position ->
            isTileContentOfType(position, clazz)).toArray();
    }

    public int[] listEmptyPositionsWithPadding() {
        return IntStream.range(0, getSize()).filter( position ->
            isTileEmpty(position) && !hasAdjacentFilledTile(position)
        ).toArray();
    }

    public boolean hasAdjacentFilledTile(int pos) {
        return IntStream.of(listAdjacentTiles(pos)).filter( position ->
            !isTileEmpty(position)
        ).findFirst().isPresent();
    }

    public int[] listNonEmptyPositions() {
        return IntStream.range(0, getSize()).filter( position ->
                !isTileEmpty(position)).toArray();
    }

    public int[] listEmptyPositions() {
        return listPositionsWithContentOf(Empty.class);
    }

    public int[] listBombPositions() {
        return listPositionsWithContentOf(Bomb.class);
    }

    public int[] listObstaclePositions() {
        return listPositionsWithContentOf(Obstacle.class);
    }

    public int[] listPositionWithOwner(String playerId){
        return IntStream.range(0, getSize())
                .filter( position -> playerId.equals(getTile(position).getOwnerID()))
                .toArray();
    }

    public int[] listEmptyValidPositions() {
        int[] emptyPositions = listEmptyPositions();
        int[] snakeAdjacentPositions = listPositionsAdjacentToCharacters();

        return IntStream.of(emptyPositions).filter(pos ->
            !ArrayUtils.contains(snakeAdjacentPositions, pos)
        ).toArray();
    }

    /**
     * @return a copy of the tiles
     */
    public Tile[] getTiles() {
        return ArrayUtils.clone(tiles);
    }

    /**
     * The world is represented by a single array
     */
    private Tile[] createEmptyTiles() {
        int size = getSize();
        Tile[] tiles = new Tile[size];

        IntStream.range(0, size).forEach(
                pos -> {
                    tiles[pos] = new Tile();
                }
        );
        return tiles;
    }
}
