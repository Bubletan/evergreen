package eg.model.sync.task;

import eg.model.player.Player;
import eg.net.game.out.MapLoadingPacket;
import eg.util.task.Task;

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
	}
}
