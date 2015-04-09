package eg.util.io;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public final class Bundle {
    
    private final Map<String, Object> map = new HashMap<>();
    private int byteSize = 4;
    
    public Bundle() {
    }
    
    public Bundle(byte[] bytes) {
        Buffer buffer = new Buffer(bytes);
        int size = buffer.getInt();
        while (--size >= 0) {
            String key = buffer.getString();
            switch (buffer.getByte()) {
            case 0:
                putBoolean(key, buffer.getBoolean());
                break;
            case 1:
                putByte(key, buffer.getByte());
                break;
            case 2:
                putShort(key, (short) buffer.getShort());
                break;
            case 3:
                putInt(key, buffer.getInt());
                break;
            case 4:
                putLong(key, buffer.getLong());
                break;
            case 5:
                putFloat(key, buffer.getFloat());
                break;
            case 6:
                putDouble(key, buffer.getDouble());
                break;
            case 7:
                putString(key, buffer.getString());
                break;
            }
        }
    }
    
    private Bundle put(String key, Object value, int objSize) {
        Preconditions.checkNotNull(key, "Key must not be null.");
        map.put(key, value);
        byteSize += key.length() + 2 + objSize;
        return this;
    }
    
    public Bundle putBoolean(String key, boolean value) {
        return put(key, value, 1);
    }
    
    public Bundle putByte(String key, byte value) {
        return put(key, value, 1);
    }
    
    public Bundle putShort(String key, short value) {
        return put(key, value, 2);
    }
    
    public Bundle putInt(String key, int value) {
        return put(key, value, 4);
    }
    
    public Bundle putLong(String key, long value) {
        return put(key, value, 8);
    }
    
    public Bundle putFloat(String key, float value) {
        return put(key, value, 4);
    }
    
    public Bundle putDouble(String key, double value) {
        return put(key, value, 8);
    }
    
    public Bundle putString(String key, String value) {
        Preconditions.checkNotNull(value, "Value must not be null.");
        return put(key, value, value.length() + 1);
    }
    
    private Object get(String key) {
        Preconditions.checkNotNull(key, "Key must not be null.");
        Object obj = map.get(key);
        Preconditions.checkState(obj != null, "Key does not exist.");
        return obj;
    }
    
    public boolean getBoolean(String key) {
        Object obj = get(key);
        Preconditions.checkState(obj instanceof Boolean, "Value for key is not a boolean.");
        return (boolean) obj;
    }
    
    public byte getByte(String key) {
        Object obj = get(key);
        Preconditions.checkState(obj instanceof Byte, "Value for key is not a byte.");
        return (byte) obj;
    }
    
    public short getShort(String key) {
        Object obj = get(key);
        Preconditions.checkState(obj instanceof Short, "Value for key is not a short.");
        return (short) obj;
    }
    
    public int getInt(String key) {
        Object obj = get(key);
        Preconditions.checkState(obj instanceof Integer, "Value for key is not an int.");
        return (int) obj;
    }
    
    public long getLong(String key) {
        Object obj = get(key);
        Preconditions.checkState(obj instanceof Long, "Value for key is not a long.");
        return (long) obj;
    }
    
    public float getFloat(String key) {
        Object obj = get(key);
        Preconditions.checkState(obj instanceof Float, "Value for key is not a float.");
        return (float) obj;
    }
    
    public double getDouble(String key) {
        Object obj = get(key);
        Preconditions.checkState(obj instanceof Double, "Value for key is not a double.");
        return (double) obj;
    }
    
    public String getString(String key) {
        Object obj = get(key);
        Preconditions.checkState(obj instanceof String, "Value for key is not a String.");
        return (String) obj;
    }
    
    public byte[] toBytes() {
        Buffer buffer = new Buffer(byteSize).putInt(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            buffer.putString(entry.getKey());
            Object value = entry.getValue();
            if (value instanceof Boolean) {
                buffer.putByte(0).putBoolean((boolean) value);
            } else if (value instanceof Byte) {
                buffer.putByte(1).putByte((byte) value);
            } else if (value instanceof Short) {
                buffer.putByte(2).putShort((short) value);
            } else if (value instanceof Integer) {
                buffer.putByte(3).putInt((int) value);
            } else if (value instanceof Long) {
                buffer.putByte(4).putLong((long) value);
            } else if (value instanceof Float) {
                buffer.putByte(5).putFloat((float) value);
            } else if (value instanceof Double) {
                buffer.putByte(6).putDouble((double) value);
            } else {
                buffer.putByte(7).putString((String) value);
            }
        }
        return buffer.toData();
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "[entries=" + map.size() + ",bytes=" + byteSize + "]";
    }
}
