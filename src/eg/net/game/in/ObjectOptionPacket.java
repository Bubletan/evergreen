package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class ObjectOptionPacket implements AbstractGamePacket {
    
    private final int option;
    private final int id;
    private final int x;
    private final int y;
    
    public ObjectOptionPacket(int option, int id, int x, int y) {
        this.option = option;
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    public int getOption() {
        return option;
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
