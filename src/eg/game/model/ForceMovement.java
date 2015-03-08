package eg.game.model;

public final class ForceMovement {
	
	private final Coordinate primaryDestination;
	private final int primaryDuration;
	private final Coordinate secondaryDestination;
	private final int secondaryDuration;
	private final Direction direction;
	
	public ForceMovement(Coordinate destination, int duration, Direction direction) {
		this(destination, duration - 1, destination, 1, direction);
	}
	
	public ForceMovement(Coordinate primaryDestination, int primaryDuration,
			Coordinate secondaryDestination, int secondaryDuration, Direction direction) {
		this.primaryDestination = primaryDestination;
		this.primaryDuration = primaryDuration;
		this.secondaryDestination = secondaryDestination;
		this.secondaryDuration = secondaryDuration;
		this.direction = direction;
	}
	
	public Coordinate getPrimaryDestination() {
		return primaryDestination;
	}
	
	public int getPrimaryDuration() {
		return primaryDuration;
	}
	
	public Coordinate getSecondaryDestination() {
		return secondaryDestination;
	}
	
	public int getSecondaryDuration() {
		return secondaryDuration;
	}
	
	public Direction getDirection() {
		return direction;
	}
}
