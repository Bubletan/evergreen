package eg.model;

import eg.global.WorldSector;

public abstract class Charactor {

	private int index;
	private Coordinate coordinate;
	private WorldSector sector;
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	
	public WorldSector getWorldSector() {
		return sector;
	}
	
	public void setWorldSector(WorldSector sector) {
		this.sector = sector;
	}
}
