package eg.util.attr;

import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class AttributeMap {
    
    private final AttributeSystem system;
    private final Cache<AttributeKey<?>, Object> values;
    
    protected AttributeMap(AttributeSystem system) {
        this.system = Objects.requireNonNull(system);
        this.values = CacheBuilder.newBuilder().weakKeys().build();
    }
    
    public AttributeSystem getSystem() {
        return system;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(AttributeKey<T> ident) {
        Preconditions.checkArgument(ident.getSystem() == system, "System mismatch.");
        return (T) values.getIfPresent(ident);
    }
    
    public <T> void set(AttributeKey<T> ident, T value) {
        Preconditions.checkArgument(ident.getSystem() == system, "System mismatch.");
        values.put(ident, value);
    }
    
    @SuppressWarnings("unchecked")
    public <T> void update(AttributeKey<T> ident, UnaryOperator<T> updater) {
        Preconditions.checkArgument(ident.getSystem() == system, "System mismatch.");
        Objects.requireNonNull(updater);
        values.asMap().compute(ident, (k, v) -> updater.apply((T) v));
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getAndSet(AttributeKey<T> ident, T value) {
        Preconditions.checkArgument(ident.getSystem() == system, "System mismatch.");
        return (T) values.asMap().put(ident, value);
    }
    
    public <T> T setAndGet(AttributeKey<T> ident, T value) {
        Preconditions.checkArgument(ident.getSystem() == system, "System mismatch.");
        values.put(ident, value);
        return value;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getAndUpdate(AttributeKey<T> ident, UnaryOperator<T> updater) {
        Preconditions.checkArgument(ident.getSystem() == system, "System mismatch.");
        Objects.requireNonNull(updater);
        Object[] old = new Object[1]; // reference to the old value
        values.asMap().compute(ident, (k, v) -> {
            old[0] = v;
            return updater.apply((T) v);
        });
        return (T) old[0];
    }
    
    @SuppressWarnings("unchecked")
    public <T> T updateAndGet(AttributeKey<T> ident, UnaryOperator<T> updater) {
        Preconditions.checkArgument(ident.getSystem() == system, "System mismatch.");
        Objects.requireNonNull(updater);
        return (T) values.asMap().compute(ident, (k, v) -> updater.apply((T) v));
    }
    
    @Override
    public String toString() {
        return system.keys.entrySet().stream()
                .map(e -> e.getValue().get())
                .filter(Objects::nonNull)
                .map(id -> id.name + "=" + values.getIfPresent(id))
                .collect(Collectors.joining(", ", getClass().getSimpleName() + " [", "]"));
    }
}
