package eg.util.attr;

import java.util.Objects;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class AttributeIdent<T> {
    
    private final AttributeSystem system;
    protected final String name;
    
    protected AttributeIdent(AttributeSystem system, String name) {
        this.system = Objects.requireNonNull(system);
        this.name = Objects.requireNonNull(name);
    }
    
    public AttributeSystem getSystem() {
        return system;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [name=" + name + "]";
    }
}
