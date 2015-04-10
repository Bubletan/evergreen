package eg.game.model;

import eg.Server;
import eg.game.model.npc.Npc;
import eg.game.model.player.Player;
import eg.game.world.Coordinate;
import eg.game.world.sync.SyncBlockSet;
import eg.util.RandomUtils;

public abstract class Charactor {
    
    private int index;
    private final Movement movement = new Movement(new Coordinate(3200 + RandomUtils.randomInt(-32, 32),
            3200 + RandomUtils.randomInt(-32, 32)));
    private boolean active;
    private SyncBlockSet syncBlockSet = new SyncBlockSet();
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public Movement getMovement() {
        return movement;
    }
    
    public Coordinate getCoordinate() {
        return movement.getCoordinate();
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
        if (this instanceof Player) {
            if (active) {
                Server.world().addPlayer((Player) this);
            } else {
                Server.world().removePlayer((Player) this);
            }
        } else {
            if (active) {
                Server.world().addNpc((Npc) this);
            } else {
                Server.world().removeNpc((Npc) this);
            }
        }
    }
    
    public SyncBlockSet getSyncBlockSet() {
        return syncBlockSet;
    }
    
    public void resetSyncBlockSet() {
        syncBlockSet = new SyncBlockSet();
    }
}
