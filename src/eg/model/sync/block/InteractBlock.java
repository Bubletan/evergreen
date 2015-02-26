package eg.model.sync.block;

import eg.model.Charactor;
import eg.model.sync.SyncBlock;

public final class InteractBlock extends SyncBlock {
	
	private final Charactor target;
	
	public InteractBlock(Charactor target) {
		this.target = target;
	}
	
	public Charactor getTarget() {
		return target;
	}
}
