package eg.game.world;

import java.util.Objects;

/**
 * Represents a three-dimensional (x, y, height) coordinate in the world.
 * 
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class Coordinate {
    
    private final int x;
    private final int y;
    private final int height;
    
    public Coordinate(int hash) {
        this(hash >> 15 & 0x7fff, hash & 0x7fff, hash >> 30);
    }
    
    public Coordinate(int x, int y) {
        this(x, y, 0);
    }
    
    public Coordinate(int x, int y, int height) {
        if (height < 0 || height > 3) {
            throw new IllegalArgumentException("Height must be between 0 and 3.");
        }
        if (x < 0 || x > 0x7fff || y < 0 || y > 0x7fff) {
            throw new IllegalArgumentException("X and Y must be between 0 and 0x7fff.");
        }
        this.x = x;
        this.y = y;
        this.height = height;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Distance getDistance(Coordinate other) {
        Objects.requireNonNull(other, "Other must not be null.");
        int dx = x - other.x;
        int dy = y - other.y;
        int value =  dx == 0 ? dy : dy == 0 ? dx : (int) Math.ceil(Math.sqrt(dx * dx + dy * dy));
        return new Distance(value);
    }
    
    public Distance getBoxDistance(Coordinate other) {
        Objects.requireNonNull(other, "Other must not be null.");
        int dx = x - other.x;
        int dy = y - other.y;
        int value = Math.max(Math.abs(dx), Math.abs(dy));
        return new Distance(value);
    }
    
    public Direction getDirection(Coordinate other) {
        Objects.requireNonNull(other, "Other must not be null.");
        int dx = other.x - x;
        int dy = other.y - y;
        return Direction.get(dx, dy);
    }
    
    public Coordinate translate(int dx, int dy) {
        return translate(dx, dy, 0);
    }
    
    public Coordinate translate(int dx, int dy, int dheight) {
        if (dx == 0 && dy == 0 && dheight == 0) {
            return this;
        }
        return new Coordinate(x + dx, y + dy, height + dheight);
    }
    
    public Coordinate toAbove() {
        if (height == 3) {
            throw new IllegalStateException("You cannot go above the height 3.");
        }
        return new Coordinate(x, y, height + 1);
    }
    
    public Coordinate toBelow() {
        if (height == 0) {
            throw new IllegalStateException("You cannot go below the height 0.");
        }
        return new Coordinate(x, y, height - 1);
    }
    
    public Coordinate toDirection(Direction direction) {
        Objects.requireNonNull(direction, "Direction must not be null.");
        if (direction == Direction.UNKNOWN) {
            throw new IllegalArgumentException("Direction must not be unknown.");
        }
        return new Coordinate(x + direction.getDeltaX(), y + direction.getDeltaY(), height);
    }
    
    @Override
    public int hashCode() {
        return height << 30 | x << 15 | y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Coordinate) {
            Coordinate other = (Coordinate) obj;
            return height == other.height && x == other.x && y == other.y;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [x=" + x + ", y=" + y + ", height=" + height + "]";
    }
}
