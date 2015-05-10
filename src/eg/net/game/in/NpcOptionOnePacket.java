package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class NpcOptionOnePacket implements AbstractGamePacket {
    
    private final int index;
    
    public NpcOptionOnePacket(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}
