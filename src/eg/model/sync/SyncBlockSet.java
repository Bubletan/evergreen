package eg.model.sync;

import java.util.HashMap;
import java.util.Map;

public final class SyncBlockSet implements Cloneable {
	
	private final Map<Class<? extends SyncBlock>, SyncBlock> blocks = new HashMap<>(8);
	
	public void add(SyncBlock block) {
		Class<? extends SyncBlock> clazz = block.getClass();
		blocks.put(clazz, block);
	}
	
	public void clear() {
		blocks.clear();
	}
	
	@Override
	public SyncBlockSet clone() {
		SyncBlockSet copy = new SyncBlockSet();
		copy.blocks.putAll(blocks);
		return copy;
	}
	
	public boolean contains(Class<? extends SyncBlock> clazz) {
		return blocks.containsKey(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends SyncBlock> T get(Class<T> clazz) {
		return (T) blocks.get(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends SyncBlock> T remove(Class<? extends SyncBlock> clazz) {
		return (T) blocks.remove(clazz);
	}
	
	public int size() {
		return blocks.size();
	}
}
