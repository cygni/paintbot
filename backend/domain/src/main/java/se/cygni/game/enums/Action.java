package se.cygni.game.enums;

public enum Action {
    LEFT(true),RIGHT(true),UP(true),DOWN(true),STAY(false),EXPLODE(false);

    private boolean isMovement;

    Action(boolean isMovement){
        this.isMovement = isMovement;
    }

    public boolean isMovement() {
        return isMovement;
    }
}
