package eg.script;

import java.util.HashMap;
import java.util.Map;

public final class ScriptArguments {
	
	private Map<String, Object> data;
	
	public ScriptArguments(String[] keys, Object[] values) {
		data = new HashMap<>();
		for (int i = 0, length = keys.length; i < length; i++) {
			data.put(keys[i], values[i]);
		}
	}
	
	public <T> T get(String key) {
		return (T) data.get(key);
	}
}
