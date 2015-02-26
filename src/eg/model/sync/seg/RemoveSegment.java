package eg.model.sync.seg;

import eg.model.sync.SyncBlockSet;
import eg.model.sync.SyncSegment;

public final class RemoveSegment extends SyncSegment {
	
	private static final SyncBlockSet EMPTY_BLOCK_SET = new SyncBlockSet();
	
	public RemoveSegment() {
		super(EMPTY_BLOCK_SET);
	}
}
