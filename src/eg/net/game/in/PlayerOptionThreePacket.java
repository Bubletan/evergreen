package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class PlayerOptionThreePacket implements AbstractGamePacket {
    
    private final int index;
    
    public PlayerOptionThreePacket(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}
