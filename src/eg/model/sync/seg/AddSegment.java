package eg.model.sync.seg;

import eg.model.Coordinate;
import eg.model.sync.SyncBlockSet;
import eg.model.sync.SyncSegment;

public final class AddSegment extends SyncSegment {
	
	private final int index;
	private final Coordinate coord;
	private final Coordinate origin;
	
	public AddSegment(SyncBlockSet blockSet, int index, Coordinate coord, Coordinate origin) {
		super(blockSet);
		this.index = index;
		this.coord = coord;
		this.origin = origin;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Coordinate getCoord() {
		return coord;
	}
	
	public Coordinate getOrigin() {
		return origin;
	}
}
