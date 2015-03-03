package eg.model;

public final class Hit {
	
	public static final int TYPE_MISS = 0;
	public static final int TYPE_DEFAULT = 1;
	public static final int TYPE_POISON = 2;
	public static final int TYPE_DISEASE = 3;
	
	private final int damage;
	private final int type;
	
	public Hit(int damage) {
		this(damage, TYPE_DEFAULT);
	}
	
	public Hit(int damage, int type) {
		if (type < 0 || type > 3) {
			throw new IllegalArgumentException("Type out of range.");
		}
		this.damage = damage;
		this.type = damage != 0 ? type : TYPE_MISS;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getType() {
		return type;
	}
}
