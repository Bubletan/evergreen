package eg;

import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import eg.game.InitializationTask;
import eg.game.ShutdownHookTask;
import eg.game.model.player.PlayerProcessTask;
import eg.game.world.World;
import eg.game.world.sync.task.NpcSyncTask;
import eg.game.world.sync.task.PlayerSyncTask;
import eg.game.world.sync.task.PostNpcSyncTask;
import eg.game.world.sync.task.PostPlayerSyncTask;
import eg.game.world.sync.task.PreNpcSyncTask;
import eg.game.world.sync.task.PrePlayerSyncTask;
import eg.net.GameServer;
import eg.script.ScriptManager;
import eg.util.task.Task;
import eg.util.task.Tasks;

public final class Server {
    
    private static final int CYCLE_RATE_MILLIS = 600;
    
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static long start;
    private static int cycle;
    private static volatile boolean exit;
    
    private static final World world = new World();
    private static final Queue<Task> tasks = new LinkedBlockingQueue<>();
    
    private static final Runnable process = () -> {
        long time = System.nanoTime();
        cycle++;
        
        world.syncLists();
        
        int nTasks = tasks.size();
        while (nTasks-- > 0) {
            Tasks.syncExec(tasks.remove());
        }
        
        world.getPlayerStream().map(PlayerProcessTask::new).forEach(Tasks::syncExec);
        
        world.getPlayerStream().map(PrePlayerSyncTask::new).forEach(Tasks::syncExec);
        world.getNpcStream().map(PreNpcSyncTask::new).forEach(Tasks::syncExec);
        
        world.getPlayerStream().map(p -> Tasks.toSequentialTask(new PlayerSyncTask(p), new NpcSyncTask(p)))
                .forEach(Tasks::syncExec);
        
        world.getPlayerStream().map(PostPlayerSyncTask::new).forEach(Tasks::syncExec);
        world.getNpcStream().map(PostNpcSyncTask::new).forEach(Tasks::syncExec);
        
        if (cycle % 100 == 0) {
            // TODO process minutely
        }
        
        if (Config.DEBUG && cycle % 10 == 0) {
            
            time = System.nanoTime() - time;
            float ms = time / 1000000f;
            float percent = 100 * ms / CYCLE_RATE_MILLIS;
            
            int memk = (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L);
            
            System.out.println("time: " + String.format("%.2f", ms) + " ms (" + String.format("%.2f", percent) + " %)"
                    + " - mem: " + String.format("%.2f", memk / 1024f) + " M"
                    + " - players: " + world.getPlayerCount());
        }
        
        if (exit) {
            executor.shutdown();
            System.gc();
            System.exit(0);
        }
    };
    
    private Server() {
    }
    
    public static void init(String[] args) {
        
        System.out.println("Initializing...");
        
        new InitializationTask().execute();
        Runtime.getRuntime().addShutdownHook(Tasks.toThread(new ShutdownHookTask()));
        
        new ScriptManager().loadScripts("./script/");
        
        new GameServer().bind(Config.PORT);
        
        start = System.currentTimeMillis();
        executor.scheduleAtFixedRate(process, CYCLE_RATE_MILLIS, CYCLE_RATE_MILLIS, TimeUnit.MILLISECONDS);
        
        // TODO: ControlPanel.launch(ControlPanel.class);
        System.out.println("Online!");
    }
    
    public static void exit() {
        exit = true;
    }
    
    /**
     * Returns the time at the beginning of the current cycle.
     */
    public static long time() {
        return start + uptime();
    }
    
    /**
     * Returns the uptime at the beginning of the current cycle.
     */
    public static long uptime() {
        return cycle * 600L;
    }
    
    /**
     * Returns the index of the current cycle.
     */
    public static int cycle() {
        return cycle;
    }
    
    /**
     * Executes the task at the beginning of the next cycle.
     */
    public static void exec(Task task) {
        tasks.add(task);
    }
    
    public static World world() {
        return world;
    }
    
    public static void main(String[] args) {
        System.setOut(new Logger(System.out));
        if (false) System.setErr(new Logger(System.err));
        init(args);
    }
}
