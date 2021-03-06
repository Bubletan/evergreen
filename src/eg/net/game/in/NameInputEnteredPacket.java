package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class NameInputEnteredPacket implements AbstractGamePacket {
    
    private final long hash;
    
    public NameInputEnteredPacket(long hash) {
        this.hash = hash;
    }
    
    public long getHash() {
        return hash;
    }
}
