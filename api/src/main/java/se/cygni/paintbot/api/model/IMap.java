package se.cygni.paintbot.api.model;

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
