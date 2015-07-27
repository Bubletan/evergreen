package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class ByteConfigPacket implements AbstractGamePacket {
    
    private final int id;
    private final byte value;
    
    public ByteConfigPacket(int id, byte value) {
        this.id = id;
        this.value = value;
    }
    
    public int getId() {
        return id;
    }
    
    public byte getValue() {
        return value;
    }
}
