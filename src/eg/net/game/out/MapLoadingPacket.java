package eg.net.game.out;

import eg.model.Coordinate;
import eg.net.game.AbstractGamePacket;

public final class MapLoadingPacket implements AbstractGamePacket {
	
	private final Coordinate coord;
	
	public MapLoadingPacket(Coordinate coord) {
		this.coord = coord;
	}
	
	public Coordinate getCoord() {
		return coord;
	}
}
