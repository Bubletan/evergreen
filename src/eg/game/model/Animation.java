package eg.game.model;

public final class Animation {
	
	public static final Animation YES = new Animation(855);
	public static final Animation NO = new Animation(856);
	public static final Animation THINKING = new Animation(857);
	public static final Animation BOW = new Animation(858);
	public static final Animation ANGRY = new Animation(859);
	public static final Animation CRY = new Animation(860);
	public static final Animation LAUGH = new Animation(861);
	public static final Animation CHEER = new Animation(862);
	public static final Animation WAVE = new Animation(863);
	public static final Animation BECKON = new Animation(864);
	public static final Animation CLAP = new Animation(865);
	public static final Animation DANCE = new Animation(866);
	public static final Animation PANIC = new Animation(2105);
	public static final Animation JIG = new Animation(2106);
	public static final Animation SPIN = new Animation(2107);
	public static final Animation HEADBANG = new Animation(2108);
	public static final Animation JOYJUMP = new Animation(2109);
	public static final Animation RASPBERRY = new Animation(2110);
	public static final Animation YAWN = new Animation(2111);
	public static final Animation SALUTE = new Animation(2112);
	public static final Animation SHRUG = new Animation(2113);
	public static final Animation BLOW_KISS = new Animation(1368);
	public static final Animation GLASS_WALL = new Animation(1128);
	public static final Animation LEAN = new Animation(1129);
	public static final Animation CLIMB_ROPE = new Animation(1130);
	public static final Animation GLASS_BOX = new Animation(1131);
	public static final Animation GOBLIN_BOW = new Animation(2127);
	public static final Animation GOBLIN_DANCE = new Animation(2128);

	private final int id;
	private final int delay;
	
	public Animation(int id) {
		this(id, 0);
	}
	
	public Animation(int id, int delay) {
		if (id < 0 || id >= 0xffff) {
			throw new IllegalArgumentException("ID out of bounds: " + id);
		}
		if (delay < 0 || delay > 0xff) {
			throw new IllegalArgumentException("Delay out of bounds: " + delay);
		}
		this.id = id;
		this.delay = delay;
	}
	
	public int getId() {
		return id;
	}
	
	public int getDelay() {
		return delay;
	}
}
