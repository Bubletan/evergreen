package eg.game.world.sync.task;

import eg.game.model.npc.Npc;
import eg.util.task.Task;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class PostNpcSyncTask implements Task {
    
    private final Npc npc;
    
    public PostNpcSyncTask(Npc npc) {
        this.npc = npc;
    }
    
    @Override
    public void execute() {
        npc.resetSyncBlockSet();
        npc.getMovement().postSyncProcess();
    }
}
