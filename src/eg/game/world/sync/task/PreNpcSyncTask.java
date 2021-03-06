package eg.game.world.sync.task;

import eg.game.model.npc.Npc;
import eg.util.task.Task;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class PreNpcSyncTask implements Task {
    
    private final Npc npc;
    
    public PreNpcSyncTask(Npc npc) {
        this.npc = npc;
    }
    
    @Override
    public void execute() {
        npc.getMovement().preSyncProcess();
    }
}
