package eg.game.world.path;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

import eg.game.world.Direction;

public final class PathBuilder {
    
    private boolean interrupted;
    private final Deque<Path.Point> points = new ArrayDeque<>();
    private boolean origin;
    
    public PathBuilder() {
    }
    
    public PathBuilder(Path.Point origin) {
        appendOriginPoint(origin);
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
    
    public PathBuilder appendJumpPoint(Path.Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Point must not be null.");
        }
        if (interrupted) {
            return this;
        }
        if (!origin && points.isEmpty()) {
            points.add(point);
            return this;
        }
        int x = point.getX();
        int y = point.getY();
        Path.Point last;
        if (origin) {
            last = points.removeLast();
            origin = false;
        } else {
            last = points.getLast();
        }
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
            points.addLast(new Path.Point(x - dx, y - dy));
        }
        points.addLast(point);
        return this;
    }
    
    public PathBuilder appendOriginPoint(Path.Point point) {
        if (!points.isEmpty()) {
            throw new IllegalStateException("Origin must be the first point.");
        }
        points.add(point);
        origin = true;
        return this;
    }
    
    public Path toPath() {
        if (origin) {
            return new Path(Collections.emptyList());
        }
        return new Path(points);
    }
}
