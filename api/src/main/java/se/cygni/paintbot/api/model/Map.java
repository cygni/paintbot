package se.cygni.paintbot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Map implements IMap {
    final int width, height;
    final long worldTick;

    final CharacterInfo[] characterInfos;

    final int[] powerUpPositions;
    final int[] obstaclePositions;

    final CollisionInfo[] collisionInfos;
    final ExplosionInfo[] explosionInfos;

    @JsonCreator
    public Map(
            @JsonProperty("width") int width,
            @JsonProperty("height") int height,
            @JsonProperty("worldTick") long worldTick,
            @JsonProperty("characterInfos") CharacterInfo[] characterInfos,
            @JsonProperty("powerUpPositions") int[] powerUpPositions,
            @JsonProperty("obstaclePositions") int[] obstaclePositions,
            @JsonProperty("collisionInfos") CollisionInfo[] collisionInfos,
            @JsonProperty("explosionInfos") ExplosionInfo[] explosionInfos
    ) {
        this.width = width;
        this.height = height;
        this.worldTick = worldTick;
        this.characterInfos = characterInfos;
        this.powerUpPositions = powerUpPositions;
        this.obstaclePositions = obstaclePositions;
        this.collisionInfos = collisionInfos;
        this.explosionInfos = explosionInfos;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public CharacterInfo[] getCharacterInfos() {
        return characterInfos;
    }

    @Override
    public long getWorldTick() {
        return worldTick;
    }

    @Override
    public int[] getPowerUpPositions() {
        return powerUpPositions;
    }

    @Override
    public int[] getObstaclePositions() {
        return obstaclePositions;
    }

    @Override
    public CollisionInfo[] getCollisionInfos() {
        return collisionInfos;
    }

    @Override
    public ExplosionInfo[] getExplosionInfos() {
        return explosionInfos;
    }
}
