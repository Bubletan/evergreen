package eg.global;

import eg.model.player.PlayerSyncTask;
import eg.util.task.Task;

public final class ProcessTask implements Task {
	
	private static final ProcessTask INSTANCE = new ProcessTask();
	
	private ProcessTask() {
	}
	
	public static ProcessTask getProcessTask() {
		return INSTANCE;
	}
	
	@Override
	public void execute() {
		
		World.getWorld().updateLists();
		
		World.getWorld().getPlayerList().parallelStream().forEach(player -> {
			player.process();
		});
		
		new PlayerSyncTask().execute();
	}
}
