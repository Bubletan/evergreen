package eg.model.sync.block;

import eg.model.Coordinate;
import eg.model.Direction;
import eg.model.sync.SyncBlock;

public final class ForceMovementBlock extends SyncBlock {
	
	private final Direction direction;
	private final int originX;
	private final int originY;
	private final int destinationX;
	private final int destinationY;
	private final int durationX;
	private final int durationY;
	
	public ForceMovementBlock(Direction direction, int originX, int originY, int destinationX,
			int destinationY, int durationX, int durationY) {
		this.direction = direction;
		this.originX = originX;
		this.originY = originY;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
		this.durationX = durationX;
		this.durationY = durationY;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public int getOriginX() {
		return originX;
	}
	
	public int getOriginY() {
		return originY;
	}
	
	public int getDestinationX() {
		return destinationX;
	}
	
	public int getDestinationY() {
		return destinationY;
	}
	
	public int getDurationX() {
		return durationX;
	}
	
	public int getDurationY() {
		return durationY;
	}
}
