package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class MainInterfacePacket implements AbstractGamePacket {
    
    private final int id;
    
    public MainInterfacePacket(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}
