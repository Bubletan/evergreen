package eg.game.model;

public final class Hit {
	
	public static enum Type {
		
		MISS(0), DEFAULT(1), POISON(2), DISEASE(3);
		
		private final int value;
		
		private Type(int value) {
			this.value = value;
		}
		
		public int toInt() {
			return value;
		}
	}
	
	private final int damage;
	private final Type type;
	
	public Hit(int damage) {
		this(damage, damage != 0 ? Type.DEFAULT : Type.MISS);
	}
	
	public Hit(int damage, Type type) {
		if (damage < 0 || damage > 0xff) {
			throw new IllegalArgumentException("Damage out of bounds: " + damage);
		}
		if (type == null) {
			throw new IllegalArgumentException("Type may not be null.");
		}
		this.damage = damage;
		this.type = type;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public Type getType() {
		return type;
	}
}
