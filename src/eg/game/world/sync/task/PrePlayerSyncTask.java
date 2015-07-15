package eg.game.world.sync.task;

import eg.Server;
import eg.game.model.player.Player;
import eg.game.world.sync.SyncBlock;
import eg.net.game.out.MapLoadingPacket;
import eg.util.task.Task;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class PrePlayerSyncTask implements Task {
    
    private final Player player;
    
    public PrePlayerSyncTask(Player player) {
        this.player = player;
    }
    
    @Override
    public void execute() {
        player.getMovement().preSyncProcess();
        if (player.getMovement().isSectorChanging()) {
            player.getSession().send(new MapLoadingPacket(player.getCoordinate()));
        }
        if (player.getSyncBlockSet().contains(SyncBlock.Type.APPEARANCE)) {
            player.getSyncContext().setAppearanceCycle(Server.cycle());
        }
    }
}
