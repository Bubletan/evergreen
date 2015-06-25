package eg.util.attr;

import java.util.Objects;

import eg.util.Types;

public final class Attribute {
    
    private final String name;
    private final Class<?> type;
    private Object value;
    
    protected Attribute(String name, Class<?> type) {
        if (type == void.class) {
            throw new IllegalArgumentException("Type void is not allowed.");
        }
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.value = Types.defaultValueOf(type);
    }
    
    public String getName() {
        return name;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }
    
    public void setValue(Object value) {
        if (!Types.isValueOf(type, value)) {
            throw new IllegalArgumentException("Value " + value + " is not applicable to type " + type + ".");
        }
        this.value = value;
    }
}
