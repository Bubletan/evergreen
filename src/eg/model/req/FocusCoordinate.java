package eg.model.req;

import eg.model.Coordinate;

public final class FocusCoordinate {

	private final int x;
	private final int y;
	
	public FocusCoordinate(Coordinate coordinate) {
		this(coordinate.getX(), coordinate.getY());
	}
	
	public FocusCoordinate(int x, int y) {
		this.x = x << 1 | 1;
		this.y = y << 1 | 1;
	}
	
	public final int getX() {
		return x;
	}
	
	public final int getY() {
		return y;
	}
}
