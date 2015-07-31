package eg.game.world;

/**
 * Represents a distance between two points in the world.
 * 
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class Distance implements Comparable<Distance> {
    
    public static final Distance ZERO = new Distance(0);
    public static final Distance ONE = new Distance(1);
    
    private final int value;
    
    public Distance(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Distance cannot be negative.");
        }
        this.value = value;
    }
    
    @Override
    public int compareTo(Distance other) {
        return Integer.compare(value, other.value);
    }
    
    @Override
    public int hashCode() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Distance && ((Distance) obj).value == value;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [value=" + value + "]";
    }
}
