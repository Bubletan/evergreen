package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class CameraPositionPacket implements AbstractGamePacket {
    
    private final int x;
    private final int y;
    private final int height;
    private final int transition;
    private final int glide;
    
    public CameraPositionPacket(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.transition = 0;
        this.glide = 100;
    }
    
    public CameraPositionPacket(int x, int y, int height, int transition, int glide) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.transition = transition;
        this.glide = glide;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getTransition() {
        return transition;
    }
    
    public int getGlide() {
        return glide;
    }
}
