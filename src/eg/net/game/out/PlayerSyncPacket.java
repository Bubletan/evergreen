package eg.net.game.out;

import java.util.List;

import eg.model.sync.SyncSegment;
import eg.net.game.AbstractGamePacket;

public final class PlayerSyncPacket implements AbstractGamePacket {
	
	private final SyncSegment localSegment;
	private final int localPlayersCount;
	private final List<SyncSegment> nonLocalSegments;
	
	public PlayerSyncPacket(SyncSegment localSegment, int localPlayersCount,
			List<SyncSegment> nonLocalSegments) {
		this.localSegment = localSegment;
		this.localPlayersCount = localPlayersCount;
		this.nonLocalSegments = nonLocalSegments;
	}
	
	public SyncSegment getLocalSegment() {
		return localSegment;
	}
	
	public int getLocalPlayersCount() {
		return localPlayersCount;
	}
	
	public List<SyncSegment> getNonLocalSegments() {
		return nonLocalSegments;
	}
}
