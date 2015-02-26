package eg.model.sync.block;

import eg.model.Coordinate;
import eg.model.sync.SyncBlock;

public final class TurnBlock extends SyncBlock {
	
	private final Coordinate target;
	
	public TurnBlock(Coordinate target) {
		this.target = target;
	}
	
	public Coordinate getTarget() {
		return target;
	}
}
