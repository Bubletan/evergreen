package eg.util.attr;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AttributeSet {
    
    private final Map<String, Attribute> map = new ConcurrentHashMap<>();
    
    public AttributeSet() {
    }
    
    public Attribute getAttribute(String name) {
        Attribute attr = map.get(name);
        if (attr == null) {
            throw new AttributeException("Attribute \"" + name + "\" does not exist.");
        }
        return attr;
    }
    
    public Attribute declareAttribute(String name) {
        Attribute attr = new Attribute(name);
        Attribute prev = map.putIfAbsent(name, attr);
        if (prev != null) {
            throw new AttributeException("Attribute \"" + name + "\" already exists.");
        }
        return attr;
    }
    
    public Attribute getOrDeclareAttribute(String name) {
        return map.computeIfAbsent(name, Attribute::new);
    }
    
    public Attribute removeAttribute(String name) {
        Attribute attr = map.remove(name);
        if (attr == null) {
            throw new AttributeException("Attribute \"" + name + "\" does not exist.");
        }
        return attr;
    }
}
