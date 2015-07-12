package eg.net.game.out;

import java.util.List;
import java.util.Objects;

import eg.game.world.Coordinate;
import eg.game.world.sync.SyncSection;
import eg.net.game.AbstractGamePacket;

public final class NpcSyncPacket implements AbstractGamePacket {
    
    private final Coordinate origin;
    private final List<SyncSection> sections;
    private final int localNpcCount;
    
    public NpcSyncPacket(Coordinate origin, List<SyncSection> sections, int localNpcCount) {
        this.origin = Objects.requireNonNull(origin);
        this.sections = Objects.requireNonNull(sections);
        this.localNpcCount = localNpcCount;
    }
    
    public Coordinate getOrigin() {
        return origin;
    }
    
    public List<SyncSection> getSections() {
        return sections;
    }
    
    public int getLocalNpcCount() {
        return localNpcCount;
    }
}
