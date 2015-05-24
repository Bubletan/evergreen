package eg.util.attr;

public final class AttributeException extends RuntimeException {
    
    private static final long serialVersionUID = -1975500914643377194L;

    public AttributeException() {
        super();
    }
    
    public AttributeException(String name) {
        super(name);
    }
}
