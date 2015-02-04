package eg.global;

import eg.util.task.Task;

public final class ShutdownHookTask implements Task {
	
	private static final ShutdownHookTask INSTANCE = new ShutdownHookTask();
	
	private ShutdownHookTask() {
	}
	
	public static ShutdownHookTask getShutdownHookTask() {
		return INSTANCE;
	}
	
	@Override
	public void execute() {
	}
}
