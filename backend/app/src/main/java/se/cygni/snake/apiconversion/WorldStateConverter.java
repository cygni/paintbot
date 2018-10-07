package se.cygni.snake.apiconversion;

import se.cygni.game.WorldState;
import se.cygni.game.worldobject.CharacterImpl;
import se.cygni.snake.api.model.CharacterInfo;
import se.cygni.snake.api.model.Map;
import se.cygni.snake.player.IPlayer;

import java.util.Set;

public class WorldStateConverter {

    public static Map convertWorldState(WorldState ws, long worldTick, Set<IPlayer> players) {

        int width = ws.getWidth();
        int height = ws.getHeight();

        CharacterInfo[] characterInfos = getCharacterInfos(ws, players);
        int[] foods = ws.listBombPositions();
        int[] obstacles = ws.listObstaclePositions();

        return new Map(
                width,
                height,
                worldTick,
                characterInfos,
                foods,
                obstacles);
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
        } catch (Exception e) {}

        return new CharacterInfo(name, points, id, -1, ws.listPositionWithOwner(player.getPlayerId()), 0);
    }

    private static CharacterInfo getCharacterInfo(WorldState ws, CharacterImpl character) {
        String name = character.getName();
        String id = character.getPlayerId();

        int position = ws.getCharacterPosition(character);

        return new CharacterInfo(name, character.getPoints(), id, position, ws.listPositionWithOwner(character.getPlayerId()), character.getIsStunnedForTicks());
    }

}
