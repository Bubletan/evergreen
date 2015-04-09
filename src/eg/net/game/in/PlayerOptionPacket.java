package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class PlayerOptionPacket implements AbstractGamePacket { 
    
    private final int option;
    private final int index;
    
    public PlayerOptionPacket(int option, int index) {
        this.option = option;
        this.index = index;
    }
    
    public int getOption() {
        return option;
    }
    
    public int getIndex() {
        return index;
    }
}
