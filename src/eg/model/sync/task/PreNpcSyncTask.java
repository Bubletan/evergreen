package eg.model.sync.task;

import eg.model.npc.Npc;
import eg.util.task.Task;

public final class PreNpcSyncTask implements Task {
	
	private final Npc npc;
	
	public PreNpcSyncTask(Npc npc) {
		this.npc = npc;
	}
	
	@Override
	public void execute() {
		npc.getMovement().preSyncProcess();
	}
}
