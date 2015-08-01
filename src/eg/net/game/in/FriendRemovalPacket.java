package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class FriendRemovalPacket implements AbstractGamePacket {
    
    private final long hash;
    
    public FriendRemovalPacket(long hash) {
        this.hash = hash;
    }
    
    public long getHash() {
        return hash;
    }
}
