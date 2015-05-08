package eg.game.world.sync.task;

import eg.Server;
import eg.game.event.impl.MovementEvent;
import eg.game.model.player.Player;
import eg.util.task.Task;

public final class PostPlayerSyncTask implements Task {
    
    private final Player player;
    
    public PostPlayerSyncTask(Player player) {
        this.player = player;
    }
    
    @Override
    public void execute() {
        if (player.getMovement().isMoving()) {
            Server.world().getEventDispatcher().dispatchEvent(new MovementEvent(player, player.getCoordinate()));
        }
        player.getMovement().postSyncProcess();
        player.resetSyncBlockSet();
    }
}
