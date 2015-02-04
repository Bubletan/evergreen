package eg.model.object;

public final class ObjectType {
	
	private static ObjectType[] cache;
	
	private final String name;
	private final byte[] desc;
	
	private ObjectType(String name, String desc) {
		this.name = name;
		this.desc = desc.getBytes();
	}
	
	public static void load() {
		cache = new ObjectType[0];
		// TODO
	}
	
	public static ObjectType get(int id) {
		if (id < 0 || id >= cache.length) {
			throw new IllegalArgumentException("ID out of range: " + id);
		}
		return cache[id];
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return new String(desc);
	}
}
