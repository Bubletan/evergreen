package eg.model.npc;

public final class NpcType {
	
	private static NpcType[] cache;

	private final int id;
	private final String name;
	private final byte[] desc;
	private final int combatLvl;
	private final int health;
	
	private NpcType(int id, String name, String desc, int combatLvl, int health) {
		this.id = id;
		this.name = name;
		this.desc = desc.getBytes();
		this.combatLvl = combatLvl;
		this.health = health;
	}
	
	public static void load() {
		cache = new NpcType[0];
		// TODO
	}
	
	public static NpcType get(int id) {
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
	
	public int getCombatLevel() {
		return combatLvl;
	}
	
	public int getHealth() {
		return health;
	}
}
