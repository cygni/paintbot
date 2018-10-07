package se.cygni.game.transformation;

import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.worldobject.CharacterImpl;

import java.util.stream.IntStream;

public class DecrementTailProtection implements WorldTransformation {

    @Override
    public WorldState transform(WorldState currentWorld) {

        Tile[] tiles = currentWorld.getTiles();
        int[] headPositions = currentWorld.listPositionsWithContentOf(CharacterImpl.class);

        IntStream.of(headPositions).forEach( headPosition -> {
            CharacterImpl character = (CharacterImpl)tiles[headPosition].getContent();
            character.decrementStun();
        });

        return new WorldState(currentWorld.getWidth(), currentWorld.getHeight(), tiles);
    }
}
