package eg.game.model.npc;

import eg.game.model.MobileEntity;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class Npc extends MobileEntity {
    
    private final NpcType type;
    private final NpcSize size;
    
    public Npc(NpcType type, NpcSize size) {
        this.type = type;
        this.size = size;
    }
    
    public NpcType getType() {
        return type;
    }
    
    public NpcSize getSize() {
        return size;
    }
}
