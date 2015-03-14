package eg.game.model;

import eg.Server;
import eg.game.WorldSector;
import eg.game.model.npc.Npc;
import eg.game.model.player.Player;
import eg.game.sync.SyncBlockSet;
import eg.util.Misc;

public abstract class Charactor {
    
    private int index;
    private final Movement movement = new Movement(new Coordinate(3200 + Misc.random(-32, 32),
            3200 + Misc.random(-32, 32)));
    private WorldSector sector;
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
    
    public WorldSector getWorldSector() {
        return sector;
    }
    
    public void setWorldSector(WorldSector sector) {
        this.sector = sector;
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