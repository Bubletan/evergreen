package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class MouseClickPacket implements AbstractGamePacket {
    
    private final long interval;
    private final int button;
    private final int x;
    private final int y;
    
    public MouseClickPacket(long interval, int button, int x, int y) {
        this.interval = interval;
        this.button = button;
        this.x = x;
        this.y = y;
    }
    
    public long getInterval() {
        return interval;
    }
    
    public boolean isLeftClick() {
        return button == 0;
    }
    
    public boolean isRightClick() {
        return button == 1;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}
