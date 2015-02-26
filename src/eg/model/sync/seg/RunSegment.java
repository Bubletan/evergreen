package eg.model.sync.seg;

import eg.model.Direction;
import eg.model.sync.SyncBlockSet;
import eg.model.sync.SyncSegment;

public final class RunSegment extends SyncSegment {
	
	private final Direction dir1;
	private final Direction dir2;
	
	public RunSegment(SyncBlockSet blockSet, Direction dir1, Direction dir2) {
		super(blockSet);
		this.dir1 = dir1;
		this.dir2 = dir2;
	}
	
	public Direction getPrimaryDirection() {
		return dir1;
	}
	
	public Direction getSecondaryDirection() {
		return dir2;
	}
}
