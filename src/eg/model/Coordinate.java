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
	
	/**
	 * Gets the X-coordinate of the 64x64 map square.
	 */
	public int getMapsquareX() {
		return x >> 6;
	}
	
	/**
	 * Gets the Y-coordinate of the 64x64 map square.
	 */
	public int getMapsquareY() {
		return y >> 6;
	}
	
	/**
	 * Gets the tile X-coordinate relative to the map square.
	 */
	public int getSubMapsquareX() {
		return x & 63;
	}
	
	/**
	 * Gets the tile Y-coordinate relative to the map square.
	 */
	public int getSubMapsquareY() {
		return y & 63;
	}
	
	public int getRegionX() {
		return x >> 3;
	}
	
	public int getRegionY() {
		return y >> 3;
	}
	
	public int getRegionX2() {
		return (x >> 3) - 6;
	}
	
	public int getRegionY2() {
		return (y >> 3) - 6;
	}
	
	public int getLocalX() {
		return (x & 7) + 48;
	}
	
	public int getLocalY() {
		return (y & 7) + 48;
	}
	
	public int getCentralRegionX() {
		return x >> 3;
	}
	
	public int getCentralRegionY() {
		return y >> 3;
	}
	
	public int getTopLeftRegionX() {
		return (x >> 3) - 6;
	}
	
	public int getTopLeftRegionY() {
		return (y >> 3) - 6;
	}
	
	public int getRelativeX(Coordinate other) {
		return x - (other.getTopLeftRegionX() << 3);
	}
	
	public int getRelativeY(Coordinate other) {
		return y - (other.getTopLeftRegionY() << 3);
	}
	
	public Coordinate translate(int dx, int dy) {
		return translate(dx, dy, 0);
	}
	
	public Coordinate translate(int dx, int dy, int dplane) {
		return new Coordinate(x + dx , y + dy, height + dplane);
	}
	
	public Coordinate at(int x, int y) {
		if (this.x != x || this.y != y) {
			return new Coordinate(x, y, height);
		}
		return this;
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
		return getClass().getName() + "[x=" + x + ",y=" + y + ",height=" + height + "]";
	}
}
