package eg.net.game.out;

import eg.game.model.Coordinate;
import eg.net.game.AbstractGamePacket;

public final class MapLoadingPacket implements AbstractGamePacket {
	
	private final Coordinate coordinate;
	
	public MapLoadingPacket(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	
	public Coordinate getCoordinate() {
		return coordinate;
	}
}
