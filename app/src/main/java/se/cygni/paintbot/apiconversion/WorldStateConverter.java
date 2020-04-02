package se.cygni.paintbot.apiconversion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.game.WorldState;
import se.cygni.game.worldobject.CharacterImpl;
import se.cygni.paintbot.api.model.CharacterInfo;
import se.cygni.paintbot.api.model.CollisionInfo;
import se.cygni.paintbot.api.model.ExplosionInfo;
import se.cygni.paintbot.api.model.Map;
import se.cygni.paintbot.player.IPlayer;

import java.util.Set;

public class WorldStateConverter {

    private static final Logger log = LoggerFactory.getLogger(WorldStateConverter.class);


    public static Map convertWorldState(WorldState ws, long worldTick, Set<IPlayer> players) {

        int width = ws.getWidth();
        int height = ws.getHeight();

        CharacterInfo[] characterInfos = getCharacterInfos(ws, players);
        int[] foods = ws.listPowerUpPositions();
        int[] obstacles = ws.listObstaclePositions();
        CollisionInfo[] colissionInfos = ws.getCollisions().entrySet().stream()
                .map(e -> new CollisionInfo(e.getKey(), e.getValue().toArray(String[]::new)))
                .toArray(CollisionInfo[]::new);

        ExplosionInfo[] explosionInfos = ws.getExplosions().entrySet().stream()
                .map(e -> new ExplosionInfo(e.getKey(), e.getValue().toArray(String[]::new)))
                .toArray(ExplosionInfo[]::new);


        return new Map(
                width,
                height,
                worldTick,
                characterInfos,
                foods,
                obstacles,
                colissionInfos,
                explosionInfos);
    }

    private static CharacterInfo[] getCharacterInfos(WorldState ws, Set<IPlayer> players) {

        CharacterInfo[] characterInfos = new CharacterInfo[players.size()];

        int c = 0;
        for (IPlayer player : players) {
            characterInfos[c++] = getCharacterInfo(ws, player);
        }

        return characterInfos;
    }

    private static CharacterInfo getCharacterInfo(WorldState ws, IPlayer player) {
        String name = player.getName();
        String id = player.getPlayerId();
        int points = player.getTotalPoints();

        try {
            CharacterImpl character = ws.getCharacterById(id);
            return getCharacterInfo(ws, character);
        } catch (Exception e) {
            log.error("Unexpected error when creating character info for " + id, e);
        }

        return new CharacterInfo(name, points, id, 10, false, ws.listPositionWithOwner(player.getPlayerId()), 0);
    }

    private static CharacterInfo getCharacterInfo(WorldState ws, CharacterImpl character) {
        String name = character.getName();
        String id = character.getPlayerId();

        int position = ws.getCharacterPosition(character);
        int[] colouredPositions = ws.listPositionWithOwner(character.getPlayerId());

        return new CharacterInfo(name, character.getPoints(), id, position, character
                .isCarryingPowerUp(), colouredPositions, character
                .getIsStunnedForTicks());
    }

}
