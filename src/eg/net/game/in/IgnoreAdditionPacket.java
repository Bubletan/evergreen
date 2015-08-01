package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class IgnoreAdditionPacket implements AbstractGamePacket {
    
    private final long hash;
    
    public IgnoreAdditionPacket(long hash) {
        this.hash = hash;
    }
    
    public long getHash() {
        return hash;
    }
}
