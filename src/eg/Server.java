package eg;

import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
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
import eg.util.task.Task;
import eg.util.task.Tasks;

public final class Server {
    
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static int cycle;
    private static volatile boolean exit;
    
    private static final World world = new World();
    private static final Queue<Task> tasks = new LinkedBlockingDeque<>();
    
    private static final Runnable process = () -> {
        long time = System.nanoTime();
        cycle++;
        
        world.syncLists();
        
        int nTasks = tasks.size();
        while (nTasks-- > 0) {
            Tasks.syncExec(tasks.remove());
        }
        
        world.getPlayerStream().map(p -> new PlayerProcessTask(p)).forEach(Tasks::syncExec);
        
        world.getPlayerStream().map(p -> new PrePlayerSyncTask(p)).forEach(Tasks::syncExec);
        world.getNpcStream().map(n -> new PreNpcSyncTask(n)).forEach(Tasks::syncExec);
        
        world.getPlayerStream().map(p -> Tasks.toSequentialTask(new PlayerSyncTask(p), new NpcSyncTask(p)))
                .forEach(Tasks::syncExec);
        
        world.getPlayerStream().map(p -> new PostPlayerSyncTask(p)).forEach(Tasks::syncExec);
        world.getNpcStream().map(n -> new PostNpcSyncTask(n)).forEach(Tasks::syncExec);
        
        if (cycle % 100 == 0) {
            // TODO process minutely
        }
        
        if (Config.DEBUG && cycle % 10 == 0) {
            
            time = System.nanoTime() - time;
            float ms = time / 1000000f;
            float percent = 100 * ms / Config.CYCLE_RATE_MILLIS;
            
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
        
        new InitializationTask().execute();
        Runtime.getRuntime().addShutdownHook(Tasks.toThread(new ShutdownHookTask()));
        
        new GameServer().bind(Config.PORT);
        
        executor.scheduleAtFixedRate(process, Config.CYCLE_RATE_MILLIS, Config.CYCLE_RATE_MILLIS, TimeUnit.MILLISECONDS);
        
        // TODO: ControlPanel.launch(ControlPanel.class);
        System.out.println("Online!");
    }
    
    public static void exit() {
        exit = true;
    }
    
    public static int cycle() {
        return cycle;
    }
    
    public static void exec(Task task) {
        tasks.add(task);
    }
    
    public static World world() {
        return world;
    }
    
    public static void main(String[] args) {
        System.setOut(new Logger(System.out));
        System.setErr(new Logger(System.err));
        init(args);
    }
}
