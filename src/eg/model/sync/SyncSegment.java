package eg.model.sync;

public abstract class SyncSegment {
	
	private final SyncBlockSet blockSet;
	
	public SyncSegment(SyncBlockSet blockSet) {
		this.blockSet = blockSet;
	}
	
	public SyncBlockSet getBlockSet() {
		return blockSet;
	}
}
