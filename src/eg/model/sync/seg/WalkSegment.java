package eg.model.sync.seg;

import eg.model.Direction;
import eg.model.sync.SyncBlockSet;
import eg.model.sync.SyncSegment;

public final class WalkSegment extends SyncSegment {
	
	private final Direction dir;
	
	public WalkSegment(SyncBlockSet blockSet, Direction dir) {
		super(blockSet);
		this.dir = dir;
	}
	
	public Direction getDirection() {
		return dir;
	}
}
