package eg.model.npc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import eg.Config;

public final class NpcType {
	
	private static final NpcType[] cache = new NpcType[Config.N_NPC_TYPES];

	private int id;
	private String name;
	private String desc;
	private int combat;
	private int health;
	
	private NpcType() {
	}
	
	public static void load() {
		int count = 0;
		
		Gson gson = new Gson();
		for (File file : new File("./data/config/npc/").listFiles()) {
			
			String name = file.getName().toLowerCase();
			if (!name.endsWith(".json")) {
				continue;
			}
			
			try {
				int id = Integer.parseInt(name.substring(0, name.length() - 5));
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				NpcType type = gson.fromJson(br, NpcType.class);
				
				type.id = id;
				cache[id] = type;
				
				count++;
				
			} catch (Exception e) {
			}
		}
		
		System.out.println("Registered " + count + " out of " + cache.length + " NPC types.");
	}
	
	public static NpcType get(int id) {
		Preconditions.checkElementIndex(id, cache.length, "ID out of bounds: " + id);
		return cache[id];
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public int getCombatLevel() {
		return combat;
	}
	
	public int getHealth() {
		return health;
	}
}
