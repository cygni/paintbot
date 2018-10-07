package se.cygni.game;

import se.cygni.game.worldobject.Empty;
import se.cygni.game.worldobject.WorldObject;

public class Tile {

    private final WorldObject content;
    private final String ownerID;

    public Tile () {
        content = new Empty();
        ownerID = null;
    }

    public Tile(WorldObject content) {
        this.content = content;
        ownerID = null;
    }

    public Tile(WorldObject content, String ownerID) {
        this.content = content;
        this.ownerID = ownerID;
    }

    public Tile(Tile copy) {
        this(copy.getContent(), copy.getOwnerID());
    }

    public WorldObject getContent() {
        return content;
    }

    public String getOwnerID() {
        return ownerID;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "content=" + content +
                ", ownerID=" + ownerID +
                '}';
    }
}
