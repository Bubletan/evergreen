package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class PlayerOptionTwoPacket implements AbstractGamePacket {
    
    private final int index;
    
    public PlayerOptionTwoPacket(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}
