package eg.game.world.path;

import java.util.ArrayDeque;
import java.util.Deque;

import eg.game.world.Direction;
import eg.game.world.path.Path.Point;

public final class PathBuilder {
    
    private boolean interrupted;
    private final Deque<Point> points = new ArrayDeque<>();
    
    public PathBuilder() {
    }
    
    public PathBuilder appendPath(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null.");
        }
        if (interrupted || path.getLength() == 0) {
            return this;
        }
        if (!points.isEmpty()) {
            appendJumpPoint(path.getPoint(0));
            if (interrupted) {
                return this;
            }
            points.removeLast();
        }
        points.addAll(path.getPoints());
        return this;
    }
    
    public PathBuilder appendPath(Path path, int beginIndex, int endIndex) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null.");
        }
        if (interrupted) {
            return this;
        }
        if (beginIndex == 0 && endIndex == path.getLength() - 1) {
            return appendPath(path);
        }
        if (beginIndex < 0 || beginIndex >= path.getLength()) {
            throw new ArrayIndexOutOfBoundsException("Begin index: " + beginIndex);
        }
        if (beginIndex == endIndex) {
            return appendJumpPoint(path.getPoint(beginIndex));
        }
        if (endIndex < 0 || endIndex >= path.getLength()) {
            throw new ArrayIndexOutOfBoundsException("End index: " + endIndex);
        }
        if (!points.isEmpty()) {
            appendJumpPoint(path.getPoint(beginIndex));
            if (interrupted) {
                return this;
            }
        } else {
            points.addLast(path.getPoint(beginIndex));
        }
        if (beginIndex < endIndex) {
            for (int i = beginIndex + 1; i <= endIndex; i++) {
                points.addLast(path.getPoint(i));
            }
        } else {
            for (int i = beginIndex - 1; i >= endIndex; i--) {
                points.addLast(path.getPoint(i));
            }
        }
        return this;
    }
    
    public PathBuilder appendJumpPoint(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Point must not be null.");
        }
        if (interrupted) {
            return this;
        }
        if (points.isEmpty()) {
            points.add(point);
            return this;
        }
        int x = point.getX();
        int y = point.getY();
        Point last = points.getLast();
        int dx = x - last.getX();
        int dy = y - last.getY();
        if (dx == 0 && dy == 0) {
            return this;
        }
        if (!Direction.connectable(dx, dy)) {
            interrupted = true;
            return this;
        }
        int max = Math.max(Math.abs(dx), Math.abs(dy)) - 1;
        for (int i = 0; i < max; i++) {
            if (dx < 0) {
                dx++;
            } else if (dx > 0) {
                dx--;
            }
            if (dy < 0) {
                dy++;
            } else if (dy > 0) {
                dy--;
            }
            points.addLast(new Point(x - dx, y - dy));
        }
        points.addLast(point);
        return this;
    }
    
    public Path toPath() {
        return new Path(points);
    }
}
