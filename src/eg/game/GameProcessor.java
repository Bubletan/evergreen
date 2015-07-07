package eg.game;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import eg.util.task.Task;

public final class GameProcessor {
    
    private static final long CYCLE_TIME = 600;
    
    private final ScheduledExecutorService processExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ForkJoinPool backgroundExecutor = new ForkJoinPool();
    
    private final Queue<Task> tasks = new LinkedBlockingQueue<>();
    
    private Date startDate;
    private Date shutdownDate;
    
    private final Task processTask = new ProcessTask();
    
    private int cycle;
    
    public GameProcessor() {
    }
    
    public void syncExec(Task task) {
        Objects.requireNonNull(task);
        tasks.add(task);
    }
    
    public void asyncExec(Task task) {
        Objects.requireNonNull(task);
        backgroundExecutor.execute(() -> {
            try {
                task.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    public void start() {
        startDate = new Date();
        processExecutor.scheduleAtFixedRate(() -> {
            long startTime = System.currentTimeMillis();
            
            int nTasks = tasks.size();
            while (nTasks-- > 0) {
                Task task = tasks.remove();
                try {
                    task.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            try {
                processTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            long time = System.currentTimeMillis() - startTime;
            if (time > CYCLE_TIME) {
                System.err.println("Cycle time out of bounds: " + time);
            }
            
            cycle++;
        }, 0, CYCLE_TIME, TimeUnit.MILLISECONDS);
    }
    
    public void shutdown() {
        processExecutor.shutdown();
        try {
            while (!processExecutor.awaitTermination(CYCLE_TIME, TimeUnit.MILLISECONDS)); // should terminate within cycle time
        } catch (InterruptedException e) {
        }
        while (!backgroundExecutor.awaitQuiescence(5000, TimeUnit.MILLISECONDS)); // wait for all the background tasks to complete
        backgroundExecutor.shutdown();
        shutdownDate = new Date();
    }
    
    public int getCycle() {
        return cycle;
    }
    
    public Optional<Date> getStartDate() {
        return Optional.ofNullable(startDate);
    }
    
    public Optional<Date> getShutdownDate() {
        return Optional.ofNullable(shutdownDate);
    }
}
