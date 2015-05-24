package eg.util.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public final class Randoms {
    
    private Randoms() {
    }
    
    public static int fromIntArray(int... array) {
        if (Objects.requireNonNull(array).length == 0) {
            throw new IllegalArgumentException("Array must not be empty.");
        }
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }
    
    public static long fromLongArray(long... array) {
        if (Objects.requireNonNull(array).length == 0) {
            throw new IllegalArgumentException("Array must not be empty.");
        }
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }
    
    public static double fromDoubleArray(double... array) {
        if (Objects.requireNonNull(array).length == 0) {
            throw new IllegalArgumentException("Array must not be empty.");
        }
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }
    
    @SafeVarargs
    public static <T> T fromArray(T... array) {
        if (Objects.requireNonNull(array).length == 0) {
            throw new IllegalArgumentException("Array must not be empty.");
        }
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }
    
    public static <T> T fromIterable(Iterable<T> iterable) {
        Objects.requireNonNull(iterable);
        if (iterable instanceof List) {
            List<T> list = (List<T>) iterable;
            if (list.isEmpty()) {
                throw new IllegalArgumentException();
            }
            return list.get(ThreadLocalRandom.current().nextInt(list.size()));
        }
        if (iterable instanceof Collection) {
            Collection<T> collection = (Collection<T>) iterable;
            if (collection.isEmpty()) {
                throw new IllegalArgumentException();
            }
            int index = ThreadLocalRandom.current().nextInt(collection.size());
            Iterator<T> it = collection.iterator();
            while (index-- > 0) {
                it.next();
            }
            return it.next();
        }
        Iterator<T> it = iterable.iterator();
        if (!it.hasNext()) {
            throw new IllegalArgumentException();
        }
        List<T> list = new ArrayList<T>();
        do {
            list.add(it.next());
        } while (it.hasNext());
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
    
    public static int fromIntRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min must not be more than max.");
        }
        if (min == max) {
            return min;
        }
        if (max == Integer.MAX_VALUE) {
            if (min == Integer.MIN_VALUE) {
                return ThreadLocalRandom.current().nextInt();
            }
            return ThreadLocalRandom.current().nextInt(min - 1, Integer.MAX_VALUE) + 1;
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    public static long fromLongRange(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("Min must not be more than max.");
        }
        if (min == max) {
            return min;
        }
        if (min >= Integer.MIN_VALUE && max <= Integer.MAX_VALUE) {
            return fromIntRange((int) min, (int) max);
        }
        if (max == Long.MAX_VALUE) {
            if (min == Long.MIN_VALUE) {
                return ThreadLocalRandom.current().nextLong();
            }
            return ThreadLocalRandom.current().nextLong(min - 1L, Long.MAX_VALUE) + 1L;
        }
        return ThreadLocalRandom.current().nextLong(min, max + 1L);
    }
    
    public static double fromDoubleRange(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min must not be more than max.");
        }
        if (min == max) {
            return min;
        }
        if (max == Double.MAX_VALUE) {
            if (min == -Double.MAX_VALUE) {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                double r1 = -random.nextDouble(-Double.MAX_VALUE, Double.MIN_VALUE);
                double r2 = -random.nextDouble(-Double.MAX_VALUE, Double.MIN_VALUE);
                return -Double.MAX_VALUE + r1 + r2;
            }
            return Math.nextUp(ThreadLocalRandom.current().nextDouble(Math.nextDown(min), Double.MAX_VALUE));
        }
        return ThreadLocalRandom.current().nextDouble(min, Math.nextUp(max));
    }
}
