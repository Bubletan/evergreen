package eg.game;

import eg.game.sync.task.NpcSyncTask;
import eg.game.sync.task.PlayerSyncTask;
import eg.game.sync.task.PostNpcSyncTask;
import eg.game.sync.task.PostPlayerSyncTask;
import eg.game.sync.task.PreNpcSyncTask;
import eg.game.sync.task.PrePlayerSyncTask;
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
		World.getWorld().getNpcStream().map(n -> new PreNpcSyncTask(n)).forEach(Tasks::execute);
		
		World.getWorld().getPlayerStream()
				.map(p -> Tasks.toSequentialTask(new PlayerSyncTask(p), new NpcSyncTask(p)))
				.forEach(Tasks::execute);
		
		World.getWorld().getPlayerStream().map(p -> new PostPlayerSyncTask(p)).forEach(Tasks::execute);
		World.getWorld().getNpcStream().map(n -> new PostNpcSyncTask(n)).forEach(Tasks::execute);
	}
}
