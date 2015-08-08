package eg.util.attr;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class AttributeSystem {
    
    protected final ConcurrentMap<String, WeakReference<AttributeIdent<?>>> idents = new ConcurrentHashMap<>();
    
    public AttributeSystem() {
    }
    
    public <T> AttributeIdent<T> newIdent(String name) {
        Objects.requireNonNull(name);
        AttributeIdent<T> ident = new AttributeIdent<>(this, name);
        WeakReference<AttributeIdent<?>> old = idents.get(name);
        if (old != null) {
            if (old.get() != null || !idents.replace(name, old, new WeakReference<>(ident))) {
                throw new IllegalArgumentException("Identifier \"" + name + "\" already exists.");
            }
        } else if (idents.putIfAbsent(name, new WeakReference<>(ident)) != null) {
            throw new IllegalArgumentException("Identifier \"" + name + "\" already exists.");
        }
        return ident;
    }
    
    public AttributeMap newMap() {
        return new AttributeMap(this);
    }
}
