package eg.util.attr;

import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public final class AttributeMap {
    
    private final AttributeSystem system;
    private final Cache<AttributeIdent<?>, Object> values;
    
    protected AttributeMap(AttributeSystem system) {
        this.system = Objects.requireNonNull(system);
        this.values = CacheBuilder.newBuilder().weakKeys().build();
    }
    
    public AttributeSystem getSystem() {
        return system;
    }
    
    public <T> T get(AttributeIdent<T> ident) {
        if (ident.getSystem() != system) {
            throw new IllegalArgumentException("System mismatch.");
        }
        @SuppressWarnings("unchecked")
        T value = (T) values.getIfPresent(ident);
        return value;
    }
    
    public <T> void set(AttributeIdent<T> ident, T value) {
        if (ident.getSystem() != system) {
            throw new IllegalArgumentException("System mismatch.");
        }
        values.put(ident, value);
    }
    
    @Override
    public String toString() {
        return system.idents.entrySet().stream()
                .map(e -> e.getValue().get())
                .filter(Objects::nonNull)
                .map(id -> id.name + "=" + values.getIfPresent(id))
                .collect(Collectors.joining(", ", getClass().getSimpleName() + " [", "]"));
    }
}
