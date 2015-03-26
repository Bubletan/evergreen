package eg.util.task;

import java.util.Arrays;
import java.util.Collection;

import eg.Server;

public final class Tasks {
    
    private Tasks() {
    }
    
    /**
     * Executes the task immediately and returns when it has been finished.
     */
    public static void syncExec(Task task) {
        task.execute();
    }
    
    /**
     * Queues the task to be executed in the next server cycle.
     */
    public static void asyncExec(Task task) {
        Server.exec(task);
    }
    
    public static Thread toThread(Task task) {
        return new Thread(task::execute);
    }
    
    public static Runnable toRunnable(Task task) {
        return task::execute;
    }
    
    public static Task toParallelTask(Task... tasks) {
        return () -> Arrays.stream(tasks).parallel().forEach(Task::execute);
    }
    
    public static Task toParallelTask(Collection<Task> tasks) {
        return () -> tasks.parallelStream().forEach(Task::execute);
    }
    
    public static Task toSequentialTask(Task... tasks) {
        return () -> Arrays.stream(tasks).forEach(Task::execute);
    }
    
    public static Task toSequentialTask(Collection<Task> tasks) {
        return () -> tasks.forEach(Task::execute);
    }
}
