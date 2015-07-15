package eg.util;

import java.beans.Introspector;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class Beans {
    
    private Beans() {
    }
    
    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
    
    public static String decapitalize(String name) {
        return Introspector.decapitalize(name);
    }
}
