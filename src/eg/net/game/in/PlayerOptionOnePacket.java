package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class PlayerOptionOnePacket implements AbstractGamePacket {
    
    private final int index;
    
    public PlayerOptionOnePacket(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}
