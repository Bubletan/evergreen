package eg.model.object;

import java.io.BufferedReader;
import java.io.FileReader;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import eg.Config;
import eg.model.npc.NpcType;

public final class ObjectType {
	
	private static final ObjectType[] cache = new ObjectType[Config.N_OBJECT_TYPES];
	
	private int id;
	private String name;
	private String desc;
	private byte[] descAsBytes;
	
	private ObjectType() {
	}
	
	public static ObjectType get(int id) {
		Preconditions.checkArgument(id >= 0 && id < cache.length, "ID out of range: " + id);
		ObjectType type = cache[id];
		if (type == null) {
			try {
				BufferedReader br = new BufferedReader(new FileReader("./data/config/object/" + id + ".json"));
				type = new Gson().fromJson(br, ObjectType.class);
				cache[id] = type;
				type.id = id;
				type.descAsBytes = type.desc.getBytes();
				type.desc = null;
			} catch (Exception e) {
				throw new RuntimeException("Error loading object type: " + id, e);
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
}
