package eg.model;

import eg.global.WorldSector;
import eg.model.npc.Npc;
import eg.model.player.Player;

public abstract class Charactor {

	private int index;
	private Coordinate coord;
	private WorldSector sector;
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public Coordinate getCoord() {
		return coord;
	}
	
	public void setCoord(Coordinate coord) {
		this.coord = coord;
	}
	
	public WorldSector getWorldSector() {
		return sector;
	}
	
	public void setWorldSector(WorldSector sector) {
		this.sector = sector;
	}
}
