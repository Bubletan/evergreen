package eg.game.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

public final class MovementProvider {
	
	private final static int MAX_SIZE = 128;
	
	private boolean buildingPath, firstJumpPoint, buildingInterrupted;
	
	private Position currentPosition;
	private Direction firstDirection, secondDirection;
	
	private final Deque<Point> oldPoints = new ArrayDeque<>();
	private final Deque<Point> points = new ArrayDeque<>();
	private boolean running;
	
	public MovementProvider(int x, int y) {
		currentPosition = new Position(x, y);
		firstDirection = secondDirection = Direction.NONE;
	}
	
	public void setPosition(int x, int y) {
		currentPosition = new Position(x, y);
	}
	
	public int getCurrentX() {
		return currentPosition.x;
	}
	
	public int getCurrentY() {
		return currentPosition.y;
	}
	
	public Direction getPrimaryDir() {
		return firstDirection;
	}
	
	public Direction getSecondaryDir() {
		return secondDirection;
	}
	
	public void beginPath(boolean run) {
		if (buildingPath) {
			throw new IllegalStateException("Path is already being built.");
		}
		buildingPath = firstJumpPoint = true;
		running = run;
	}
	
	public void addJumpPoint(int x, int y) {
		if (!buildingPath) {
			throw new IllegalStateException("Path is not being built.");
		}
		if (buildingInterrupted) {
			return;
		}
		if (firstJumpPoint) {
			firstJumpPoint = false;
			int dx = x - currentPosition.x;
			int dy = y - currentPosition.y;
			if (Direction.isConnectable(dx, dy)) {
				points.clear();
				oldPoints.clear();
				addJumpPointAndFill(new Position(x, y));
				return;
			} else {
				Queue<Position> travelBackQueue = new ArrayDeque<>();
				Point oldPoint;
				while ((oldPoint = oldPoints.pollLast()) != null) {
					Position oldPosition = oldPoint.pos;
					dx = oldPosition.x - currentPosition.x;
					dy = oldPosition.y - currentPosition.y;
					travelBackQueue.add(oldPosition);
					if (Direction.isConnectable(dx, dy)) {
						points.clear();
						oldPoints.clear();
						for (Position travelBackPosition : travelBackQueue) {
							addJumpPointAndFill(travelBackPosition);
						}
						addJumpPointAndFill(new Position(x, y));
						return;
					}
				}
				oldPoints.clear();
				buildingInterrupted = true;
			}
		} else {
			addJumpPointAndFill(new Position(x, y));
		}
	}
	
	private void addJumpPointAndFill(Position pos) {
		int x = pos.x, y = pos.y;
		Point last = getLast();
		int deltaX = x - last.pos.x;
		int deltaY = y - last.pos.y;
		int max = Math.max(Math.abs(deltaX), Math.abs(deltaY)) - 1;
		for (int i = 0; i < max; i++) {
			if (deltaX < 0) {
				deltaX++;
			} else if (deltaX > 0) {
				deltaX--;
			}
			if (deltaY < 0) {
				deltaY++;
			} else if (deltaY > 0) {
				deltaY--;
			}
			addStep(new Position(x - deltaX, y - deltaY));
		}
		addStep(pos);
	}
	
	private void addStep(Position pos) {
		if (points.size() >= MAX_SIZE) {
			return;
		}
		Point last = getLast();
		int dx = pos.x - last.pos.x;
		int dy = pos.y - last.pos.y;
		Direction dir = Direction.forDeltas(dx, dy);
		if (dir != Direction.NONE) {
			Point point = new Point(pos, dir);
			points.add(point);
			oldPoints.add(point);
		}
	}
	
	private Point getLast() {
		Point last = points.peekLast();
		if (last == null) {
			return new Point(currentPosition, Direction.NONE);
		}
		return last;
	}
	
	public void endPath() {
		if (!buildingPath) {
			throw new IllegalStateException("Path is not being built.");
		}
		buildingPath = buildingInterrupted = false;
	}
	
	public void clearPath() {
		points.clear();
		oldPoints.clear();
	}
	
	public int getSize() {
		return points.size();
	}
	
	public void nextMoment() {
		Position position = currentPosition;
		Direction first = Direction.NONE, second = Direction.NONE;
		Point next = points.poll();
		if (next != null) {
			first = next.dir;
			position = next.pos;
			if (running /* and enough energy */) {
				next = points.poll();
				if (next != null) {
					second = next.dir;
					position = next.pos;
				}
			}
			currentPosition = position;
		}
		firstDirection = first;
		secondDirection = second;
	}
	
	private static final class Point {
		
		private final Position pos;
		private final Direction dir;
		
		public Point(Position pos, Direction dir) {
			this.pos = pos;
			this.dir = dir;
		}
	}
	
	private static final class Position {
		
		private final int x;
		private final int y;
		
		private Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
