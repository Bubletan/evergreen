package eg.game;

import eg.Server;
import eg.game.model.player.PlayerProcessTask;
import eg.game.world.World;
import eg.game.world.sync.task.NpcSyncTask;
import eg.game.world.sync.task.PlayerSyncTask;
import eg.game.world.sync.task.PostNpcSyncTask;
import eg.game.world.sync.task.PostPlayerSyncTask;
import eg.game.world.sync.task.PreNpcSyncTask;
import eg.game.world.sync.task.PrePlayerSyncTask;
import eg.util.task.Task;
import eg.util.task.Tasks;

/**
 * A task executed once per cycle.
 * 
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class ProcessTask implements Task {
    
    private static final World world = Server.world();
    
    @Override
    public void execute() {
        
        world.syncLists();
        
        
        world.getPlayerStream().map(PlayerProcessTask::new).forEach(Tasks::exec);
        
        world.getPlayerStream().map(PrePlayerSyncTask::new).forEach(Tasks::exec);
        world.getNpcStream().map(PreNpcSyncTask::new).forEach(Tasks::exec);
        
        world.getPlayerStream().map(p -> Tasks.toSequentialTask(new PlayerSyncTask(p), new NpcSyncTask(p)))
                .forEach(Tasks::exec);
        
        world.getPlayerStream().map(PostPlayerSyncTask::new).forEach(Tasks::exec);
        world.getNpcStream().map(PostNpcSyncTask::new).forEach(Tasks::exec);
    }
}
