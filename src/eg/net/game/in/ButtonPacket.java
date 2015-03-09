package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class ButtonPacket implements AbstractGamePacket {
    
    private final int id;
    
    public ButtonPacket(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}
