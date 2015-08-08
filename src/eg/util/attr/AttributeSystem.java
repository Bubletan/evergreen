package eg.util.attr;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class AttributeSystem {
    
    protected final ConcurrentMap<String, WeakReference<AttributeKey<?>>> keys = new ConcurrentHashMap<>();
    
    public AttributeSystem() {
    }
    
    public <T> AttributeKey<T> newKey(String name) {
        Objects.requireNonNull(name);
        AttributeKey<T> ident = new AttributeKey<>(this, name);
        WeakReference<AttributeKey<?>> old = keys.get(name);
        if (old != null) {
            if (old.get() != null || !keys.replace(name, old, new WeakReference<>(ident))) {
                throw new IllegalArgumentException("Key \"" + name + "\" already exists.");
            }
        } else if (keys.putIfAbsent(name, new WeakReference<>(ident)) != null) {
            throw new IllegalArgumentException("Key \"" + name + "\" already exists.");
        }
        return ident;
    }
    
    public AttributeMap newMap() {
        return new AttributeMap(this);
    }
}
