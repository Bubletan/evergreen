package eg.util.attr;

public final class Attribute {
    
    private final String name;
    private volatile Object value;
    
    protected Attribute(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
}
