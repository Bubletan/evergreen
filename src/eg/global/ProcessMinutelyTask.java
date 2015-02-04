package eg.global;

import eg.util.task.Task;

public final class ProcessMinutelyTask implements Task {
	
	private static final ProcessMinutelyTask INSTANCE = new ProcessMinutelyTask();
	
	private ProcessMinutelyTask() {
	}
	
	public static ProcessMinutelyTask getProcessMinutelyTask() {
		return INSTANCE;
	}
	
	@Override
	public void execute() {
		
		World.getWorld().getPlayerList().parallelStream().forEach(player -> {
		});
	}
}
