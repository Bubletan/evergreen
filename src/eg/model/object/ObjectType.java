package eg.model.object;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import eg.Config;

public final class ObjectType {
	
	private static final ObjectType[] cache = new ObjectType[Config.N_OBJECT_TYPES];
	
	private int id;
	private String name;
	private String desc;
	
	private ObjectType() {
	}
	
	public static void load() {
		int count = 0;
		
		Gson gson = new Gson();
		for (File file : new File("./data/config/object/").listFiles()) {
			
			String name = file.getName().toLowerCase();
			if (!name.endsWith(".json")) {
				continue;
			}
			
			try {
				int id = Integer.parseInt(name.substring(0, name.length() - 5));
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				ObjectType type = gson.fromJson(br, ObjectType.class);
				
				type.id = id;
				cache[id] = type;
				
				count++;
				
			} catch (Exception e) {
			}
		}
		
		System.out.println("Registered " + count + " out of " + cache.length + " object types.");
	}
	
	public static ObjectType get(int id) {
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
}
