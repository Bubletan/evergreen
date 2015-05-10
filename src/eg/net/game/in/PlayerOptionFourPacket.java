package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class PlayerOptionFourPacket implements AbstractGamePacket {
    
    private final int index;
    
    public PlayerOptionFourPacket(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}
