package eg;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import eg.global.ProcessMinutelyTask;
import eg.global.ProcessTask;
import eg.global.ShutdownHookTask;
import eg.net.GameServer;

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
	
	private static ScheduledExecutorService process;
	private static int loopCycle;
	
	private Server() {
	}
	
	public static void init() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			ShutdownHookTask.getShutdownHookTask().execute();
		}));
		
		System.setOut(new Logger(System.out));
		System.setErr(new Logger(System.err));
		
		new GameServer().bind(Config.PORT);
		
		process = Executors.newSingleThreadScheduledExecutor();
		process.scheduleAtFixedRate(() -> {
			long cycleBegin = System.currentTimeMillis();
			loopCycle++;
			ProcessTask.getProcessTask().execute();
			if (loopCycle % 100 == 0) {
				ProcessMinutelyTask.getProcessMinutelyTask().execute();
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
