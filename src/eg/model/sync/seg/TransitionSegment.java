package eg.model.sync.seg;

import eg.model.Coordinate;
import eg.model.sync.SyncBlockSet;
import eg.model.sync.SyncSegment;

public final class TransitionSegment extends SyncSegment {
	
	private final Coordinate coord;
	private final boolean teleport;
	private final Coordinate lastKnownSector;
	
	public TransitionSegment(SyncBlockSet blockSet, Coordinate coord, boolean teleport,
			Coordinate lastKnownSector) {
		super(blockSet);
		this.coord = coord;
		this.teleport = teleport;
		this.lastKnownSector = lastKnownSector;
	}
	
	public Coordinate getCoord() {
		return coord;
	}
	
	public boolean isTeleporting() {
		return teleport;
	}
	
	public Coordinate getLastKnownSector() {
		return lastKnownSector;
	}
}
