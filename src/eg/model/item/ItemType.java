package eg.model.item;

public final class ItemType {
	
	private static ItemType[] cache;

	private final int id;
	private final String name;
	private final byte[] desc;
	private final boolean stackable;
	private final boolean tradable;
	private final boolean notable;
	private final int equipSlot;
	private final short[] equipBonus;
	
	private ItemType(int id, String name, String desc, boolean stackable, boolean tradable,
			boolean notable, int equipSlot, short[] equipBonus) {
		this.id = id;
		this.name = name;
		this.desc = desc.getBytes();
		this.stackable = stackable;
		this.tradable = tradable;
		this.notable = notable;
		this.equipSlot = equipSlot;
		this.equipBonus = equipBonus;
	}
	
	public static void load() {
		cache = new ItemType[0];
		// TODO
	}
	
	public static ItemType get(int id) {
		if (id < 0 || id >= cache.length) {
			throw new IllegalArgumentException("ID out of range: " + id);
		}
		return cache[id];
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return new String(desc);
	}
	
	public boolean isStackable() {
		return stackable;
	}
	
	public boolean isTradable() {
		return tradable;
	}
	
	public boolean isNotable() {
		return notable;
	}
	
	public boolean isEquipable() {
		return equipSlot != -1;
	}
	
	public int getEquipSlot() {
		return equipSlot;
	}
	
	public short[] getEquipBonuses() {
		return equipBonus;
	}
}
