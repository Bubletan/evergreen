package eg.global;

import eg.model.item.ItemType;
import eg.model.npc.NpcType;
import eg.model.object.ObjectType;
import eg.util.task.Task;

public final class InitializationTask implements Task {
	
	@Override
	public void execute() {
		
		ItemType.load();
		NpcType.load();
		ObjectType.load();
	}
}
