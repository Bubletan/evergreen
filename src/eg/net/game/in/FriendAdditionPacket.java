package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class FriendAdditionPacket implements AbstractGamePacket {
    
    private final long hash;
    
    public FriendAdditionPacket(long hash) {
        this.hash = hash;
    }
    
    public long getHash() {
        return hash;
    }
}
