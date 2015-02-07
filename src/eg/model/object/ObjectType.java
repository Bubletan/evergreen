package eg.model.object;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import eg.Config;

public final class ObjectType {
	
	private static final ObjectType[] cache = new ObjectType[Config.N_OBJECT_TYPES];
	
	private transient int id;
	private String name;
	private String desc;
	
	private ObjectType() {
	}
	
	public static ObjectType get(int id) {
		Preconditions.checkElementIndex(id, cache.length, "ID out of bounds: " + id);
		ObjectType type = cache[id];
		if (type != null) {
			return type;
		}
		synchronized (cache) {
			type = cache[id];
			if (type != null) {
				return type;
			}
			try {
				BufferedReader br = new BufferedReader(new FileReader("./data/config/object/" + id + ".json"));
				type = new Gson().fromJson(br, ObjectType.class);
				type.id = id;
				cache[id] = type;
				return type;
			} catch (Exception e) {
				throw new RuntimeException("Error loading object type: " + id);
			}
		}
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
