package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class InterfaceScrollPositionPacket implements AbstractGamePacket {
    
    private final int id;
    private final int position;
    
    public InterfaceScrollPositionPacket(int id, int position) {
        this.id = id;
        this.position = position;
    }
    
    public int getId() {
        return id;
    }
    
    public int getPosition() {
        return position;
    }
}
