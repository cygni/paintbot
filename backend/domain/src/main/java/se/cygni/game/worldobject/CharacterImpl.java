package se.cygni.game.worldobject;


public class CharacterImpl implements Character {

    private final String name;
    private final String playerId;
    private int points;
    private int position;
    private boolean isCarryingBomb;
    private int isStunnedForTicks;

    public CharacterImpl(String name, String playerId, int position) {
        this.name = name;
        this.playerId = playerId;
        this.position = position;
        this.isCarryingBomb = false;
        this.isStunnedForTicks = 0;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPlayerId() {
        return playerId;
    }

    public boolean isCarryingBomb() {
        return isCarryingBomb;
    }

    public void setCarryingBomb(boolean carryingBomb) {
        isCarryingBomb = carryingBomb;
    }

    public int getIsStunnedForTicks() {
        return isStunnedForTicks;
    }

    public void setIsStunnedForTicks(int isStunnedForTicks) {
        this.isStunnedForTicks = isStunnedForTicks;
    }

    @Override
    public void decrementStun() {
        if(isStunnedForTicks > 0) {
            isStunnedForTicks--;
        }
    }
}
