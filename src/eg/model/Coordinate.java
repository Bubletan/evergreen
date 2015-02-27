package eg.model;

public final class Coordinate {
	
	public static final Coordinate
			AL_KHARID = new Coordinate(3293, 3174),
			ARDOUGNE_EAST = new Coordinate(2662, 3305),
			ARDOUGNE_WEST = new Coordinate(2529, 3307),
			CAMELOT = new Coordinate(2757, 3477),
			DRAYNOR_VILLAGE = new Coordinate(3093, 3244),
			DUEL_ARENA = new Coordinate(3360, 3213),
			EDGEVILLE = new Coordinate(3093, 3493),
			FALADOR = new Coordinate(2964, 3378),
			LUMBRIDGE = new Coordinate(3222, 3218),
			PORT_PHASMATYS = new Coordinate(3687, 3502),
			PORT_SARIM = new Coordinate(3023, 3208),
			RIMMINGTON = new Coordinate(2957, 3214),
			VARROCK = new Coordinate(3210, 3424),
			YANILLE = new Coordinate(2606, 3093);

	private final int x;
	private final int y;
	private final int height;
	
	public Coordinate(int hash) {
		this(hash >> 15 & 0x7fff, hash & 0x7fff, hash >> 30);
	}
	
	public Coordinate(int x, int y) {
		this(x, y, 0);
	}
	
	public Coordinate(int x, int y, int height) {
		if (height < 0 || height > 3) {
			throw new IllegalArgumentException("Height must be between 0 and 3.");
		}
		if (x < 0 || x > 0x7fff || y < 0 || y > 0x7fff) {
			throw new IllegalArgumentException("X and Y must be between 0 and 0x7fff.");
		}
		this.x = x;
		this.y = y;
		this.height = height;
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
	
	public int getDistance(Coordinate coord) {
		int dx = x - coord.x;
		int dy = y - coord.y;
		return (int) Math.ceil(Math.sqrt(dx * dx + dy * dy));
	}
	
	public int getBoxDistance(Coordinate coord) {
		int dx = x - coord.x;
		int dy = y - coord.y;
		return Math.max(Math.abs(dx), Math.abs(dy));
	}
	
	public Coordinate translate(int dx, int dy) {
		return translate(dx, dy, 0);
	}
	
	public Coordinate translate(int dx, int dy, int dheight) {
		if (dx == 0 && dy == 0 && dheight == 0) {
			return this;
		}
		return new Coordinate(x + dx, y + dy, height + dheight);
	}
	
	public Coordinate above() {
		if (height == 3) {
			throw new IllegalStateException("You cannot go above the height 3.");
		}
		return new Coordinate(x, y, height + 1);
	}
	
	public Coordinate below() {
		if (height == 0) {
			throw new IllegalStateException("You cannot go below the height 0.");
		}
		return new Coordinate(x, y, height - 1);
	}
	
	@Override
	public int hashCode() {
		return height << 30 | x << 15 | y;
	}
	
	public static int hashCode(int x, int y) {
		if (x < 0 || x > 0x7fff || y < 0 || y > 0x7fff) {
			throw new IllegalArgumentException("X and Y must be between 0 and 0x7fff.");
		}
		return x << 15 | y;
	}
	
	public static int hashCode(int x, int y, int height) {
		if (height < 0 || height > 3) {
			throw new IllegalArgumentException("Height must be between 0 and 3.");
		}
		if (x < 0 || x > 0x7fff || y < 0 || y > 0x7fff) {
			throw new IllegalArgumentException("X and Y must be between 0 and 0x7fff.");
		}
		return height << 30 | x << 15 | y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Coordinate) {
			Coordinate coord = (Coordinate) obj;
			return height == coord.height && x == coord.x && y == coord.y;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [x=" + x + ", y=" + y + ", height=" + height + "]";
	}
}
