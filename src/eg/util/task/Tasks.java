package eg.util.task;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.StreamSupport;

import eg.Server;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class Tasks {
    
    private Tasks() {
    }
    
    public static void exec(Task task) {
        task.execute();
    }
    
    public static void syncExec(Task task) {
        Server.processor().syncExec(task);
    }
    
    public static void asyncExec(Task task) {
        Server.processor().asyncExec(task);
    }
    
    public static Thread toThread(Task task) {
        return new Thread(toRunnable(task));
    }
    
    public static Runnable toRunnable(Task task) {
        return task::execute;
    }
    
    public static Task fromRunnable(Runnable runnable) {
        return runnable::run;
    }
    
    public static Task toParallelTask(Task... tasks) {
        return () -> Arrays.stream(tasks).parallel().forEach(Task::execute);
    }
    
    public static Task toParallelTask(Iterable<Task> tasks) {
        if (tasks instanceof Collection) {
            Collection<Task> collection = (Collection<Task>) tasks;
            return () -> collection.parallelStream().forEach(Task::execute);
        }
        return () -> StreamSupport.stream(tasks.spliterator(), true).forEach(Task::execute);
    }
    
    public static Task toSequentialTask(Task... tasks) {
        return () -> {
            for (Task task : tasks) {
                task.execute();
            }
        };
    }
    
    public static Task toSequentialTask(Iterable<Task> tasks) {
        return () -> tasks.forEach(Task::execute);
    }
}
