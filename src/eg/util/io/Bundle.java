package eg.util.io;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public final class Bundle {
    
    private final Map<String, Object> map = new HashMap<>();
    private int byteSize = 4;
    
    public Bundle() {
    }
    
    public Bundle(byte[] data) {
        
        Buffer buf = new Buffer(data);
        int size = buf.getInt();
        
        while (--size >= 0) {
            
            String key = buf.getString();
            
            switch (buf.getByte()) {
            
            case 0:
                putBoolean(key, buf.getBoolean());
                break;
                
            case 1:
                putByte(key, buf.getByte());
                break;
                
            case 2:
                putShort(key, (short) buf.getShort());
                break;
                
            case 3:
                putInt(key, buf.getInt());
                break;
                
            case 4:
                putLong(key, buf.getLong());
                break;
                
            case 5:
                putFloat(key, buf.getFloat());
                break;
                
            case 6:
                putDouble(key, buf.getDouble());
                break;
                
            case 7:
                putString(key, buf.getString());
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
    
    public byte[] toData() {
        
        Buffer buf = new Buffer(byteSize).putInt(map.size());
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            
            buf.putString(entry.getKey());
            Object value = entry.getValue();
            
            if (value instanceof Boolean) {
                
                buf.putByte(0).putBoolean((boolean) value);
                
            } else if (value instanceof Byte) {
                
                buf.putByte(1).putByte((byte) value);
                
            } else if (value instanceof Short) {
                
                buf.putByte(2).putShort((short) value);
                
            } else if (value instanceof Integer) {
                
                buf.putByte(3).putInt((int) value);
                
            } else if (value instanceof Long) {
                
                buf.putByte(4).putLong((long) value);
                
            } else if (value instanceof Float) {
                
                buf.putByte(5).putFloat((float) value);
                
            } else if (value instanceof Double) {
                
                buf.putByte(6).putDouble((double) value);
                
            } else {
                
                buf.putByte(7).putString((String) value);
            }
        }
        return buf.toData();
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "[entries=" + map.size() + ",bytes=" + byteSize + "]";
    }
}
