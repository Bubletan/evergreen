package eg.model.player;

import eg.util.task.Task;

public final class PlayerSaveTask implements Task {
	
	private Player player;
	
	public PlayerSaveTask(Player player) {
		this.player = player;
	}
	
	@Override
	public void execute() {
	}
}
