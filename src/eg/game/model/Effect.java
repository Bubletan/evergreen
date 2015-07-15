package eg.game.model;

public final class Effect {
    
    private final int id;
    private final int height;
    private final int delay;
    
    public Effect(int id) {
        this(id, 100);
    }
    
    public Effect(int id, int height) {
        this(id, height, 0);
    }
    
    public Effect(int id, int height, int delay) {
        if (id < 0 || id >= 0xffff) {
            throw new IllegalArgumentException("ID out of bounds: " + id);
        }
        if (height < 0 || height > 0xffff) {
            throw new IllegalArgumentException("Height out of bounds: " + height);
        }
        if (delay < 0 || delay > 0xffff) {
            throw new IllegalArgumentException("Delay out of bounds: " + delay);
        }
        this.id = id;
        this.height = height;
        this.delay = delay;
    }
    
    public int getId() {
        return id;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getDelay() {
        return delay;
    }
}
