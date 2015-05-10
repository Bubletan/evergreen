package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class ObjectOptionOnePacket implements AbstractGamePacket {
    
    private final int id;
    private final int x;
    private final int y;
    
    public ObjectOptionOnePacket(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    public int getId() {
        return id;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}
