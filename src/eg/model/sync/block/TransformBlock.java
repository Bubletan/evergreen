package eg.model.sync.block;

import eg.model.npc.NpcType;
import eg.model.sync.SyncBlock;

public final class TransformBlock extends SyncBlock {
	
	private final NpcType type;
	
	public TransformBlock(NpcType type) {
		this.type = type;
	}
	
	public NpcType getType() {
		return type;
	}
}
