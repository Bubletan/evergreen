package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class NpcOptionTwoPacket implements AbstractGamePacket {
    
    private final int index;
    
    public NpcOptionTwoPacket(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}
