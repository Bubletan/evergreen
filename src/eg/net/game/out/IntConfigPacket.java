package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class IntConfigPacket implements AbstractGamePacket {
    
    private final int id;
    private final int value;
    
    public IntConfigPacket(int id, int value) {
        this.id = id;
        this.value = value;
    }
    
    public int getId() {
        return id;
    }
    
    public int getValue() {
        return value;
    }
}
