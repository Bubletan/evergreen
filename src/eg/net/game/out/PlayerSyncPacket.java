package eg.net.game.out;

import java.util.List;

import eg.model.Coordinate;
import eg.model.sync.SyncSegment;
import eg.net.game.AbstractGamePacket;

public final class PlayerSyncPacket implements AbstractGamePacket {
	
	private final SyncSegment localSegment;
	private final int localPlayersCount;
	private final Coordinate origin;
	private final List<SyncSegment> nonLocalSegments;
	
	public PlayerSyncPacket(SyncSegment localSegment, int localPlayersCount,
			Coordinate origin, List<SyncSegment> nonLocalSegments) {
		this.localSegment = localSegment;
		this.localPlayersCount = localPlayersCount;
		this.origin = origin;
		this.nonLocalSegments = nonLocalSegments;
	}
	
	public SyncSegment getLocalSegment() {
		return localSegment;
	}
	
	public int getLocalPlayersCount() {
		return localPlayersCount;
	}
	
	public Coordinate getOrigin() {
		return origin;
	}
	
	public List<SyncSegment> getNonLocalSegments() {
		return nonLocalSegments;
	}
}
