# Client Info

This document gives general information on how the client <=> server communication works as well as expectations and
general info for anyone who want's to implement their own client in a new language.

## Interfaces
It is important that the no matter which language the bot implementer chooses, i.e. which starting client the 
offered functionality should be the same. It should not matter if I choose Java or .NET in terms of functionality
offered to the bot. Below follows interfaces with documentation to serve as an outline for creating your own client. 


[MapUtility](client-util/src/main/java/se/cygni/paintbot/client/MapUtility.java)
``` 
/**
 * Utility for getting information from the map object in a bit more developer friendly format
 */
public interface MapUtility {
    /**
     * Checks if it's possible to move in the direction specified
     * @param direction the direction to check for movement possibility
     * @return  if direction is available for movement
     */
    boolean canIMoveInDirection(CharacterAction direction);

    /**
     * Returns an array of coordinates painted in the provided player's colour.
     *
     * @param playerId the id of the player
     * @return an array of MapCoordinate coloured by the player with matching playerId
     */
    MapCoordinate[] getPlayerColouredPositions(String playerId);

    /**
     * @return An array containing all MapCoordinates where there's Power ups
     */
    MapCoordinate[] listCoordinatesContainingPowerUps();

    /**
     * @return An array containing all MapCoordinates where there's an Obstacle
     */
    MapCoordinate[] listCoordinatesContainingObstacle();

    /**
     * @param coordinate to check
     * @return true if the TileContent at coordinate is Empty or contains Power Up
     */
    boolean isTileAvailableForMovementTo(MapCoordinate coordinate);

    /**
     * @return The MapCoordinate of your character
     */
    MapCoordinate getMyPosition();

    /**
     * Get the character info of the player
     * @return player's CharacterInfo
     */
    CharacterInfo getMyCharacterInfo();

    /**
     * Get character info of a specific player id
     * @param playerId the id of the player too look up
     * @return the character info of tha player wrapped in an optional
     */
    Optional<CharacterInfo> getCharacterInfoOf(String playerId);

    /**
     * @param coordinate map coordinate
     * @return whether or not it is out of bounds
     */
    boolean isCoordinateOutOfBounds(MapCoordinate coordinate);

    /**
     * @param coordinate to check
     * @return the TileContent at the specified coordinate
     */
    TileContent getTileAt(MapCoordinate coordinate);

    /**
     * Converts a position in the flattened single array representation
     * of the Map to a MapCoordinate.
     *
     * @param position to convert to coordinate
     * @return coordinate representation of position
     */
    MapCoordinate convertPositionToCoordinate(int position);

    /**
     * Converts a MapCoordinate to the same position in the flattened
     * single array representation of the Map.
     *
     * @param coordinate to convert to position
     * @return position representation of coordinate
     */
    int convertCoordinateToPosition(MapCoordinate coordinate);

    /**
     * Converts a list of positions in array format to list of coordinates.
     *
     * @param positions
     * @return
     */
    MapCoordinate[] convertPositionsToCoordinates(int[] positions);

    /**
     * Converts a list of coordinates to position array format
     * @param coordinates to convert
     * @return position list of converted positions
     */
    int[] convertCoordinatesToPositions(MapCoordinate[] coordinates);
}
```


[IMap](api/src/main/java/se/cygni/paintbot/api/model/IMap.java)

Also take a look at some relevant models in the same package, such as CharacterInfo, CollisionInfo and ExplosionInfo
``` 

/**
 * The map representing the world the bots play in
 */
public interface IMap {
    /**
     *
     * @return width of the map
     */
    int getWidth();

    /**
     *
     * @return height of the map
     */
    int getHeight();

    /**
     *
     * @return array of info for the different characters (players) on the map
     */
    CharacterInfo[] getCharacterInfos();

    /**
     *
     * @return the current world tick, game consist of X ticks.
     */
    long getWorldTick();

    /**
     *
     * @return array of positions with power ups
     */
    int[] getPowerUpPositions();

    /**
     *
     * @return array of positions with obstacles
     */
    int[] getObstaclePositions();

    /**
     *
     * @return array of positions where there is a collision occurring and which characters are involved
     */
    CollisionInfo[] getCollisionInfos();

    /**
     *
     * @return array of positions where there is an explosion occurring and which characters are causing it
     */
    ExplosionInfo[] getExplosionInfos();
}

```

## Flow
The base flow of the communication is a websocket-connection with bi-directional communication of messages.
