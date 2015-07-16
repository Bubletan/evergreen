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

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class Path implements Iterable<Coordinate> {
    
    private final List<Coordinate> points;
    
    public Path(Iterable<Coordinate> points) {
        List<Coordinate> list;
        if (points instanceof Collection) {
            list = new ArrayList<>((Collection<Coordinate>) points);
        } else {
            list = new ArrayList<>();
            points.forEach(list::add);
        }
        this.points = Collections.unmodifiableList(list);
    }
    
    @Override
    public Iterator<Coordinate> iterator() {
        return points.iterator();
    }
    
    public List<Coordinate> getPoints() {
        return points;
    }
    
    public List<Coordinate> getJumpPoints() {
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
        Coordinate before = points.get(index - 1);
        Coordinate point = points.get(index);
        Coordinate after = points.get(index + 1);
        return Direction.get(point.getX() - before.getX(), point.getY() - before.getY())
                != Direction.get(after.getX() - point.getX(), after.getY() - point.getY());
    }
    
    public Coordinate getPoint(int index) {
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
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Path)) {
            return false;
        }
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
}
