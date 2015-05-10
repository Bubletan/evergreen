package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class PlayerOptionFivePacket implements AbstractGamePacket {
    
    private final int index;
    
    public PlayerOptionFivePacket(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}
