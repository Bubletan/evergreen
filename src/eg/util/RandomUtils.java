package eg.util;

import java.util.Random;

public final class RandomUtils {
    
    private static final Random random = new Random();
    
    private RandomUtils() {
    }
    
    /**
     * Returns a random {@code int} between {@code 0} (inclusive) and {@code range} (exclusive).
     */
    public static int randomInt(int range) {
        if (range < 0) {
            throw new IllegalArgumentException("Range may not be negative.");
        }
        return random.nextInt(range);
    }
    
    /**
     * Returns a random {@code int} between {@code min} and {@code max} (both inclusive).
     */
    public static int randomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min may not be bigger than max.");
        }
        return min + random.nextInt(max - min + 1);
    }
}
