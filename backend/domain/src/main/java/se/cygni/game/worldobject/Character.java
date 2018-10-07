package se.cygni.game.worldobject;

public interface Character extends WorldObject {
    String getPlayerId();
    int getPosition();
    void setPosition(int position);
    boolean isCarryingBomb();
    void setCarryingBomb(boolean carryingBomb);
    int getIsStunnedForTicks();
    void setIsStunnedForTicks(int isStunnedForTicks);
    void decrementStun();
}
