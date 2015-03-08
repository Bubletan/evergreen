package eg.game.model.player;

import eg.util.task.Task;

public final class PlayerRestoreTask implements Task {
	
	private Player player;
	
	public PlayerRestoreTask(Player player) {
		this.player = player;
	}
	
	@Override
	public void execute() {
	}
}
