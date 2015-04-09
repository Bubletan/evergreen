package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class GameInterfacePacket implements AbstractGamePacket {
    
    private final int id;
    
    public GameInterfacePacket(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}
