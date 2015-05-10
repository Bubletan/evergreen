package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class NpcOptionThreePacket implements AbstractGamePacket {
    
    private final int index;
    
    public NpcOptionThreePacket(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}
