package eg.util;

public final class Color {
    
    private final int value;
    
    private Color(int value) {
        this.value = value;
    }
    
    public static Color rgb(int red, int green, int blue) {
        return rgba(red, green, blue, 0xff);
    }
    
    public static Color rgba(int red, int green, int blue, int alpha) {
        if (red < 0 || red > 0xff || green < 0 || green > 0xff ||
                blue < 0 || blue > 0xff || alpha < 0 || alpha > 0xff) {
            throw new IllegalArgumentException("All parameters must be within 0 to 255.");
        }
        return new Color(alpha << 24 | red << 16 | green << 8 | blue);
    }
    
    public int getRed() {
        return (value >> 16) & 0xff;
    }
    
    public int getGreen() {
        return (value >> 8) & 0xff;
    }
    
    public int getBlue() {
        return value & 0xff;
    }
    
    public int getAlpha() {
        return value >>> 24;
    }
    
    @Override
    public int hashCode() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Color && ((Color) obj).value == value;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [value=" + Integer.toHexString(value) + "]";
    }
}
