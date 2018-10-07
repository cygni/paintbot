package se.cygni.game.exception;

import se.cygni.game.worldobject.CharacterImpl;

public class SnakeCollision extends CollisionException {

    private final CharacterImpl collisionWith;

    public SnakeCollision(int position, CharacterImpl head) {
        super(position);
        this.collisionWith = head;
    }

    public CharacterImpl getCollisionWith() {
        return collisionWith;
    }
}
