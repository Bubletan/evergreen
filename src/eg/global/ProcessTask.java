package eg.global;

import eg.model.sync.task.PlayerSyncTask;
import eg.model.sync.task.PostPlayerSyncTask;
import eg.model.sync.task.PrePlayerSyncTask;
import eg.util.task.Task;
import eg.util.task.Tasks;

public final class ProcessTask implements Task {
	
	@Override
	public void execute() {
		
		World.getWorld().updateLists();
		
		World.getWorld().getPlayerStream().forEach(player -> {
			player.process();
		});
		
		World.getWorld().getPlayerStream().map(p -> new PrePlayerSyncTask(p)).forEach(Tasks::execute);
		World.getWorld().getPlayerStream().map(p -> new PlayerSyncTask(p)).forEach(Tasks::execute);
		World.getWorld().getPlayerStream().map(p -> new PostPlayerSyncTask(p)).forEach(Tasks::execute);
	}
}
