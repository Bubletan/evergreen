package eg;

import eg.game.GameProcessor;
import eg.game.StartTask;
import eg.game.ShutdownTask;
import eg.game.world.World;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class Server {
    
    private static volatile boolean exited = false;
    
    private static final World world = new World();
    
    private static final GameProcessor processor = new GameProcessor();
    
    private Server() {
    }
    
    /**
     * Returns the index of the current cycle.
     */
    public static int cycle() {
        return processor.getCycle();
    }
    
    public static World world() {
        return world;
    }
    
    public static GameProcessor processor() {
        return processor;
    }
    
    public static void exit() {
        new Thread(() -> exit(true)).start();
    }
    
    private static void init() {

        System.out.println("Initializing...");
        long time = System.currentTimeMillis();
        
        new StartTask().execute();
        
        processor.start();
        
        time = System.currentTimeMillis() - time;
        System.out.println("Initialized! (took: " + time + " ms)");
    }
    
    private static void exit(boolean callSystemExit) {
        if (exited) {
            return;
        }
        exited = true;
        
        System.out.println("Exiting...");
        long time = System.currentTimeMillis();
        
        processor.shutdown();
        new ShutdownTask().execute();
        
        time = System.currentTimeMillis() - time;
        System.out.println("Exited! (took: " + time + " ms)");
        
        
        
        System.gc();
        if (callSystemExit) {
            System.exit(0);
        }
        
    }
    
    public static void main(String[] args) {
        System.setOut(new Logger(System.out));
        if (false) System.setErr(new Logger(System.err));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Server.exit(false)));
        init();
    }
}
