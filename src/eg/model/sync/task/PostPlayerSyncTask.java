package eg.model.sync.task;

import eg.model.player.Player;
import eg.util.task.Task;

public final class PostPlayerSyncTask implements Task {
	
	private final Player player;
	
	public PostPlayerSyncTask(Player player) {
		this.player = player;
	}
	
	@Override
	public void execute() {
		player.getMovement().postSyncProcess();
		player.resetSyncBlockSet();
	}
}
