package eg.game.model;

import eg.game.World;
import eg.game.WorldSector;
import eg.game.model.npc.Npc;
import eg.game.model.player.Player;
import eg.game.sync.SyncBlockSet;

public abstract class Charactor {

	private int index;
	private final Movement movement = new Movement(new Coordinate(3200, 3200));
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
				World.getWorld().addPlayer((Player) this);
			} else {
				World.getWorld().removePlayer((Player) this);
			}
		} else {
			if (active) {
				World.getWorld().addNpc((Npc) this);
			} else {
				World.getWorld().removeNpc((Npc) this);
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
