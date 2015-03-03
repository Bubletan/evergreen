package eg.model;

public final class Effect {
	
	public static final Effect NULL = new Effect(0xffff);

	private final int id;
	private final int height;
	private final int delay;
	
	public Effect(int id) {
		this(id, 0, 0);
	}
	
	public Effect(int id, int height) {
		this(id, height, 0);
	}
	
	public Effect(int id, int height, int delay) {
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
