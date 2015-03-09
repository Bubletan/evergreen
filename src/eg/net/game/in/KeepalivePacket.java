package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class KeepalivePacket implements AbstractGamePacket {
    
    private final long time;
    
    public KeepalivePacket(long time) {
        this.time = time;
    }
    
    public long getTime() {
        return time;
    }
}
