package eg.model.npc;

import java.io.BufferedReader;
import java.io.FileReader;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import eg.Config;

public final class NpcType {
	
	private static final NpcType[] cache = new NpcType[Config.N_NPC_TYPES];

	private int id;
	private String name;
	private String desc;
	private byte[] descAsBytes;
	private int combat;
	private int health;
	
	private NpcType() {
	}
	
	public static NpcType get(int id) {
		Preconditions.checkArgument(id >= 0 && id < cache.length, "ID out of range: " + id);
		NpcType type = cache[id];
		if (type == null) {
			try {
				BufferedReader br = new BufferedReader(new FileReader("./data/config/npc/" + id + ".json"));
				type = new Gson().fromJson(br, NpcType.class);
				cache[id] = type;
				type.id = id;
				type.descAsBytes = type.desc.getBytes();
				type.desc = null;
			} catch (Exception e) {
				throw new RuntimeException("Error loading npc type: " + id, e);
			}
		}
		return type;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return new String(descAsBytes);
	}
	
	public int getCombatLevel() {
		return combat;
	}
	
	public int getHealth() {
		return health;
	}
}
