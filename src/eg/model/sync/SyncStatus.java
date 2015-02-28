package eg.model.sync;

import eg.model.Coordinate;
import eg.model.Direction;

public abstract class SyncStatus {
	
	private final Type type;
	
	private SyncStatus(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public static enum Type {
		
		TRANSITION, RUN, WALK, STAND, ADDITION, REMOVAL
	}
	
	public static final class Transition extends SyncStatus {
		
		private final Coordinate coordinate;
		private final boolean sectorChanging;
		
		public Transition(Coordinate coordinate, boolean sectorChanging) {
			super(Type.TRANSITION);
			this.coordinate = coordinate;
			this.sectorChanging = sectorChanging;
		}
		
		public Coordinate getCoordinate() {
			return coordinate;
		}
		
		public boolean isSectorChanging() {
			return sectorChanging;
		}
	}
	
	public static final class Run extends SyncStatus {
		
		private final Direction primaryDirection;
		private final Direction secondaryDirection;
		
		public Run(Direction primaryDirection, Direction secondaryDirection) {
			super(Type.RUN);
			this.primaryDirection = primaryDirection;
			this.secondaryDirection = secondaryDirection;
		}
		
		public Direction getPrimaryDirection() {
			return primaryDirection;
		}
		
		public Direction getSecondaryDirection() {
			return secondaryDirection;
		}
	}
	
	public static final class Walk extends SyncStatus {
		
		private final Direction direction;
		
		public Walk(Direction direction) {
			super(Type.WALK);
			this.direction = direction;
		}
		
		public Direction getDirection() {
			return direction;
		}
	}
	
	public static final class Stand extends SyncStatus {
		
		public Stand() {
			super(Type.STAND);
		}
	}
	
	public static final class Addition extends SyncStatus {
		
		private final int index;
		private final Coordinate coordinate;
		
		public Addition(int index, Coordinate coordinate) {
			super(Type.ADDITION);
			this.index = index;
			this.coordinate = coordinate;
		}
		
		public int getIndex() {
			return index;
		}
		
		public Coordinate getCoordinate() {
			return coordinate;
		}
	}
	
	public static final class Removal extends SyncStatus {
		
		public Removal() {
			super(Type.REMOVAL);
		}
	}
}
