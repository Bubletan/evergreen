package eg;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import eg.global.ProcessMinutelyTask;
import eg.global.ProcessTask;
import eg.global.ShutdownHookTask;
import eg.net.GameServer;
import eg.util.task.Task;
import eg.util.task.Tasks;

/**
 * Evergreen is a RuneScape Server Emulator Framework for the #317 revision.<br>
 * <br>
 * <b>References:</b>
 * <ul>
 * <li>Hyperion</li>
 * <li>Project Insanity</li>
 * <li>Apollo</li>
 * <li>BlakeScape</li>
 * <li><a href="link http://www.rune-server.org/runescape-development/rs-503-client-server/downloads/572004-basic-netty-framework.html">Basic Netty Framework</a></li>
 * </ul>
 * @author Rob / Bubletan
 * @see <a href="http://rsps.wikia.com/wiki/317_Protocol">317 Protocol - Runescape Private Server Wiki</a>
 */
public final class Server {
	
	private static final ScheduledExecutorService process = Executors.newSingleThreadScheduledExecutor();
	private static int loopCycle;
	
	private static final Task processTask = new ProcessTask();
	private static final Task processMinutelyTask = new ProcessMinutelyTask();
	
	private Server() {
	}
	
	public static void init() {
		Runtime.getRuntime().addShutdownHook(Tasks.toThread(new ShutdownHookTask()));
		
		System.setOut(new Logger(System.out));
		System.setErr(new Logger(System.err));
		
		new GameServer().bind(Config.PORT);
		
		process.scheduleAtFixedRate(() -> {
			long cycleBegin = System.currentTimeMillis();
			loopCycle++;
			processTask.execute();
			if (loopCycle % 100 == 0) {
				processMinutelyTask.execute();
			}
			long cycleEnd = System.currentTimeMillis();
			float percent = Math.round((cycleEnd - cycleBegin) / (float) Config.CYCLE_RATE_MILLIS * 10000) / 100f;
			if (false) System.out.println("cycle time: " + (cycleEnd - cycleBegin) + " ms (" + percent + " %)");
		}, Config.CYCLE_RATE_MILLIS, Config.CYCLE_RATE_MILLIS, TimeUnit.MILLISECONDS);
		
		// TODO: ControlPanel.launch(ControlPanel.class);
		System.out.println("Online!");
	}
	
	public static void exit() {
		process.shutdown();
		System.gc();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		init();
	}
}
