package eg.game.sync;

import java.util.HashMap;
import java.util.Map;

public final class SyncBlockSet implements Cloneable {
	
	private final Map<SyncBlock.Type, SyncBlock> blocks = new HashMap<>(8);
	
	public void add(SyncBlock block) {
		blocks.put(block.getType(), block);
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
	
	public boolean contains(SyncBlock.Type type) {
		return blocks.containsKey(type);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends SyncBlock> T get(SyncBlock.Type type) {
		return (T) blocks.get(type);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends SyncBlock> T remove(SyncBlock.Type type) {
		return (T) blocks.remove(type);
	}
	
	public int size() {
		return blocks.size();
	}
}
