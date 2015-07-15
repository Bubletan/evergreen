package eg.game.model.npc;

import java.io.BufferedReader;
import java.io.FileReader;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import eg.Config;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class NpcType {
    
    private static final NpcType[] cache = new NpcType[Config.N_NPC_TYPES];
    
    private transient int id;
    private String name;
    private String desc;
    private int combat;
    private int health;
    
    private NpcType() {
    }
    
    public static NpcType get(int id) {
        Preconditions.checkElementIndex(id, cache.length, "ID out of bounds: " + id);
        NpcType type = cache[id];
        if (type != null) {
            return type;
        }
        synchronized (cache) {
            type = cache[id];
            if (type != null) {
                return type;
            }
            try {
                BufferedReader br = new BufferedReader(new FileReader("./data/config/npc/" + id + ".json"));
                type = new Gson().fromJson(br, NpcType.class);
                type.id = id;
                cache[id] = type;
                return type;
            } catch (Exception e) {
                throw new RuntimeException("Error loading NPC type: " + id);
            }
        }
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return desc;
    }
    
    public int getCombatLevel() {
        return combat;
    }
    
    public int getHealth() {
        return health;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + "id=" + id + "]";
    }
}
