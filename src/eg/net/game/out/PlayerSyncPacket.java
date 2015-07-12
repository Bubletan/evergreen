package eg.net.game.out;

import java.util.List;
import java.util.Objects;

import eg.game.world.Coordinate;
import eg.game.world.sync.SyncSection;
import eg.net.game.AbstractGamePacket;

public final class PlayerSyncPacket implements AbstractGamePacket {
    
    private final SyncSection localSection;
    private final int localPlayersCount;
    private final Coordinate origin;
    private final Coordinate sectorOrigin;
    private final List<SyncSection> nonLocalSections;
    
    public PlayerSyncPacket(SyncSection localSection, int localPlayersCount,
            Coordinate origin, Coordinate sectorOrigin, List<SyncSection> nonLocalSections) {
        this.localSection = Objects.requireNonNull(localSection);
        this.localPlayersCount = localPlayersCount;
        this.origin = Objects.requireNonNull(origin);
        this.sectorOrigin = Objects.requireNonNull(sectorOrigin);
        this.nonLocalSections = Objects.requireNonNull(nonLocalSections);
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
