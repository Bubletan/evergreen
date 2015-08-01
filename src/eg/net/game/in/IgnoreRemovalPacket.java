package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class IgnoreRemovalPacket implements AbstractGamePacket {
    
    private final long hash;
    
    public IgnoreRemovalPacket(long hash) {
        this.hash = hash;
    }
    
    public long getHash() {
        return hash;
    }
}
