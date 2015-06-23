package eg.util;

import java.util.Objects;

public final class Types {
    
    private Types() {
    }
    
    @SuppressWarnings("unchecked")
    public static <T> Class<T> asBoxed(Class<T> type) {
        Objects.requireNonNull(type);
        if (type == byte.class) {
            return (Class<T>) Byte.class;
        }
        if (type == short.class) {
            return (Class<T>) Short.class;
        }
        if (type == int.class) {
            return (Class<T>) Integer.class;
        }
        if (type == long.class) {
            return (Class<T>) Long.class;
        }
        if (type == float.class) {
            return (Class<T>) Float.class;
        }
        if (type == double.class) {
            return (Class<T>) Double.class;
        }
        if (type == char.class) {
            return (Class<T>) Character.class;
        }
        if (type == boolean.class) {
            return (Class<T>) Boolean.class;
        }
        if (type == void.class) {
            return (Class<T>) Void.class;
        }
        throw new IllegalArgumentException("Only primitive types can be boxed.");
    }
    
    @SuppressWarnings("unchecked")
    public static <T> Class<T> asUnboxed(Class<T> type) {
        Objects.requireNonNull(type);
        if (type == Byte.class) {
            return (Class<T>) byte.class;
        }
        if (type == Short.class) {
            return (Class<T>) short.class;
        }
        if (type == Integer.class) {
            return (Class<T>) int.class;
        }
        if (type == Long.class) {
            return (Class<T>) long.class;
        }
        if (type == Float.class) {
            return (Class<T>) float.class;
        }
        if (type == Double.class) {
            return (Class<T>) double.class;
        }
        if (type == Character.class) {
            return (Class<T>) char.class;
        }
        if (type == Boolean.class) {
            return (Class<T>) boolean.class;
        }
        if (type == Void.class) {
            return (Class<T>) void.class;
        }
        throw new IllegalArgumentException("Only boxed primitive types can be unboxed.");
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T defaultValueOf(Class<T> type) {
        Objects.requireNonNull(type);
        if (!type.isPrimitive()) {
            return null;
        }
        if (type == byte.class) {
            return (T) Byte.valueOf((byte) 0);
        }
        if (type == short.class) {
            return (T) Short.valueOf((short) 0);
        }
        if (type == int.class) {
            return (T) Integer.valueOf(0);
        }
        if (type == long.class) {
            return (T) Long.valueOf(0L);
        }
        if (type == float.class) {
            return (T) Float.valueOf(0.0f);
        }
        if (type == double.class) {
            return (T) Double.valueOf(0.0d);
        }
        if (type == char.class) {
            return (T) Character.valueOf('\u0000');
        }
        if (type == boolean.class) {
            return (T) Boolean.FALSE;
        }
        throw new IllegalArgumentException("Type void has no default value.");
    }
    
    public static boolean isValueOf(Class<?> type, Object value) {
        Objects.requireNonNull(type);
        if (type.isPrimitive()) {
            if (value == null) {
                return false;
            }
            type = asBoxed(type);
        } else if (value == null) {
            return true;
        }
        return type.isInstance(value);
    }
}
