package se.cygni.game.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.game.Tile;
import se.cygni.game.WorldState;
import se.cygni.game.enums.Action;
import se.cygni.game.exception.TransformationException;
import se.cygni.game.worldobject.*;
import se.cygni.game.worldobject.Character;
import se.cygni.game.worldobject.CharacterImpl;

public class PerformCharacterAction implements WorldTransformation {

    private static final Logger log = LoggerFactory.getLogger(PerformCharacterAction.class);


    private CharacterImpl character;
    private Action action;
    private boolean pickedUpBomb;

    public PerformCharacterAction(CharacterImpl character, Action action) {
        this.character = character;
        this.action = action;
    }

    @Override
    public WorldState transform(WorldState currentWorld) throws TransformationException {
        if (character == null) {
            throw new TransformationException("CharacterImpl is null!!");
        }

        if (action == null) {
            throw new TransformationException("Action is null!");
        }

        int characterPosition = character.getPosition();
        int targetCharacterPosition = characterPosition;
        
        if(action.isMovement()){
            try {
                targetCharacterPosition = currentWorld.getPositionForAdjacent(characterPosition, action);
            } catch (RuntimeException re) {
                log.debug("Character {} run into a wall", character.getPlayerId());
            }
        }
        

        // Target tile is not empty, check what's in it (rember that this
        // move is in a World where only this Snake exists).
        if (!currentWorld.isTileEmpty(targetCharacterPosition)) {

            Tile targetTile = currentWorld.getTile(targetCharacterPosition);
            WorldObject targetContent = targetTile.getContent();

            //TODO: Potentially handle player collisions
            if (targetContent instanceof Bomb) {
                pickedUpBomb = true;
            }
        }

        Tile[] tiles = currentWorld.getTiles();

        updateCharacterState(tiles, targetCharacterPosition, character, pickedUpBomb);

        return new WorldState(currentWorld.getWidth(), currentWorld.getHeight(), tiles);
    }

    public boolean isBombPickedUp() {
        return pickedUpBomb;
    }

    private void updateCharacterState(Tile[] tiles, int targetPosition, Character character, boolean hasPickedUpBomb) {
        Tile currentTile = tiles[character.getPosition()];
        tiles[character.getPosition()] = new Tile(new Empty(), currentTile.getOwnerID());
        tiles[targetPosition] = new Tile(character, character.getPlayerId());
        character.setPosition(targetPosition);
        if(hasPickedUpBomb) {
            character.setCarryingBomb(true);
        }
    }
}
