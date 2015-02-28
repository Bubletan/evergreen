package eg.net.game.out;

import java.util.List;

import eg.model.Coordinate;
import eg.model.sync.SyncSection;
import eg.net.game.AbstractGamePacket;

public final class PlayerSyncPacket implements AbstractGamePacket {
	
	private final SyncSection localSection;
	private final int localPlayersCount;
	private final Coordinate origin;
	private final Coordinate sectorOrigin;
	private final List<SyncSection> nonLocalSections;
	
	public PlayerSyncPacket(SyncSection localSection, int localPlayersCount,
			Coordinate origin, Coordinate sectorOrigin, List<SyncSection> nonLocalSections) {
		this.localSection = localSection;
		this.localPlayersCount = localPlayersCount;
		this.origin = origin;
		this.sectorOrigin = sectorOrigin;
		this.nonLocalSections = nonLocalSections;
	}
	
	public SyncSection getLocalSection() {
		return localSection;
	}
	
	public int getLocalPlayersCount() {
		return localPlayersCount;
	}
	
	public Coordinate getOrigin() {
		return origin;
	}
	
	public Coordinate getSectorOrigin() {
		return sectorOrigin;
	}
	
	public List<SyncSection> getNonLocalSections() {
		return nonLocalSections;
	}
}
