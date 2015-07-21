package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class WalkableGameInterfacePacket implements AbstractGamePacket {
    
    private final int id;
    
    public WalkableGameInterfacePacket(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}
