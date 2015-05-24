package eg.game.model.npc;

import eg.game.model.MobileEntity;

public final class Npc extends MobileEntity {
    
    private final NpcType type;
    
    public Npc(NpcType type) {
        this.type = type;
    }
    
    public NpcType getType() {
        return type;
    }
}
