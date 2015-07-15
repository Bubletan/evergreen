package eg.util.attr;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class AttributeSet {
    
    private final Map<String, Attribute> map = new HashMap<>();
    
    public AttributeSet() {
    }
    
    public Attribute newAttribute(String name, Class<?> type) {
        Attribute attr = new Attribute(name, type);
        if (map.putIfAbsent(name, attr) != null) {
            throw new IllegalStateException("Attribute \"" + name + "\" already exists.");
        }
        return attr;
    }
    
    public Attribute getAttribute(String name) {
        Attribute attr = map.get(name);
        if (attr == null) {
            throw new IllegalArgumentException("Attribute \"" + name + "\" does not exist.");
        }
        return attr;
    }
    
    public Attribute removeAttribute(String name) {
        Attribute attr = map.remove(name);
        if (attr == null) {
            throw new IllegalArgumentException("Attribute \"" + name + "\" does not exist.");
        }
        return attr;
    }
}
