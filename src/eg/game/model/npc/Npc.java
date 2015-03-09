package eg.game.model.npc;

import eg.game.model.Charactor;

public final class Npc extends Charactor {
    
    private final NpcType type;
    
    public Npc(NpcType type) {
        this.type = type;
    }
    
    public NpcType getType() {
        return type;
    }
}
