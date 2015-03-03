package eg.util.task;

import java.util.Arrays;
import java.util.Collection;

public final class Tasks {
	
	private Tasks() {
	}
	
	public static void execute(Task task) {
		task.execute();
	}
	
	public static Thread toThread(Task task) {
		return new Thread(() -> task.execute());
	}
	
	public static Runnable toRunnable(Task task) {
		return () -> task.execute();
	}
	
	public static Task toParallelTask(Task... tasks) {
		return () -> Arrays.stream(tasks).parallel().forEach(t -> t.execute());
	}
	
	public static Task toParallelTask(Collection<Task> tasks) {
		return () -> tasks.parallelStream().forEach(t -> t.execute());
	}
	
	public static Task toSequentialTask(Task... tasks) {
		return () -> Arrays.stream(tasks).forEach(t -> t.execute());
	}
	
	public static Task toSequentialTask(Collection<Task> tasks) {
		return () -> tasks.stream().forEach(t -> t.execute());
	}
}
