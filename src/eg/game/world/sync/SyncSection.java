package eg.game.world.sync;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class SyncSection {
    
    private final SyncStatus status;
    private final SyncBlockSet blockSet;
    
    public SyncSection(SyncStatus status, SyncBlockSet blockSet) {
        this.status = status;
        this.blockSet = blockSet;
    }
    
    public SyncStatus getStatus() {
        return status;
    }
    
    public SyncBlockSet getBlockSet() {
        return blockSet;
    }
}
