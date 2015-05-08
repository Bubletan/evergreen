package eg.game.world.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import eg.game.world.Coordinate;
import eg.game.world.Direction;

public final class Path implements Iterable<Path.Point> {
    
    private final List<Point> points;
    
    public Path(Collection<Point> points) {
        this.points = Collections.unmodifiableList(new ArrayList<>(points));
    }
    
    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }
    
    public List<Point> getPoints() {
        return points;
    }
    
    public List<Point> getJumpPoints() {
        if (points.size() <= 2) {
            return points;
        }
        return Collections.unmodifiableList(IntStream.range(0, points.size()).filter(this::isJumpPoint)
                .mapToObj(this::getPoint).collect(Collectors.toList()));
    }
    
    private boolean isJumpPoint(int index) {
        if (index < 0 || index >= points.size()) {
            throw new ArrayIndexOutOfBoundsException("Point index out of bounds: " + index);
        }
        if (index == 0 || index == points.size() - 1) {
            return true;
        }
        Point before = points.get(index - 1);
        Point point = points.get(index);
        Point after = points.get(index + 1);
        return Direction.get(point.getX() - before.getX(), point.getY() - before.getY())
                != Direction.get(after.getX() - point.getX(), after.getY() - point.getY());
    }
    
    public Point getPoint(int index) {
        if (index < 0 || index >= points.size()) {
            throw new ArrayIndexOutOfBoundsException("Point index out of bounds: " + index);
        }
        return points.get(index);
    }
    
    public int getLength() {
        return points.size();
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [points="
                + points.stream().map(Object::toString).collect(Collectors.joining(",")) + "]";
    }
    
    @Override
    public int hashCode() {
        return points.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Path) {
            Path path = (Path) obj;
            int length = points.size();
            if (path.points.size() != length) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (!path.points.get(i).equals(points.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static final class Point {
        
        private final int x;
        private final int y;
        
        public Point(Coordinate coordinate) {
            this(coordinate.getX(), coordinate.getY());
        }
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
        
        @Override
        public int hashCode() {
            return x << 15 | y;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Point) {
                Point point = (Point) obj;
                return x == point.x && y == point.y;
            }
            return false;
        }
    }
}
