package eg.script.gen;

import eg.model.player.Player;
import eg.util.io.Buffer;
import eg.model.Movement;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generated file, do not modify.
 */
public class ScriptGen {

	public void invoke(eg.model.player.Player player$author, String label$name, eg.script.ScriptArguments args$) {
		switch (label$name) {
		case "button":
			try {
				// buttons.egs
				int id = args$.<Integer>get("id");
				player$author.message("Button: " + id);
				switch (id) {
				}
			} catch (Throwable throwable$) {
				throwable$.printStackTrace();
			}
			break;
		case "packet":
			try {
				// decode_packet.egs
				String label = null;
				String[] arg_keys = null;
				Object[] arg_values = null;
				final Buffer buf = args$.<Buffer>get("buf");
				final int type = args$.<Integer>get("type");
				final int size = args$.<Integer>get("size");
				switch (type) {
					case 103: {
						label = "command";
						arg_keys = new String[] {"cmd", "args"};
						final String line = buf.getLine();
						final boolean hasArgs = line.contains(" ");
						final String cmd = hasArgs ? line.substring(0, line.indexOf(' ')) : line;
						String[] args = null;
						if (hasArgs) {
							List<String> matchList = new ArrayList<>();
							Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
							Matcher regexMatcher = regex.matcher(line.substring(line.indexOf(' ') + 1));
							while (regexMatcher.find()) {
								if (regexMatcher.group(1) != null) {
									matchList.add(regexMatcher.group(1));
								} else if (regexMatcher.group(2) != null) {
									matchList.add(regexMatcher.group(2));
								} else {
									matchList.add(regexMatcher.group());
								}
							}
							args = matchList.toArray(new String[matchList.size()]);
						} else {
							args = new String[0];
						}
						arg_values = new Object[] {cmd, args};
					} break;
					case 185: {
						label = "button";
						arg_keys = new String[] {"id"};
						arg_values = new Object[] {buf.getShort()};
					} break;
					case 98: case 164: case 248: {
						int _size = size;
						if (type == 248) {
							_size -= 14;
						}
						final int steps = (_size - 5) >> 1;
						final int[][] path = new int[steps][2];
						final boolean run_steps = buf.getByte2() == 1;
						final int first_y = buf.getLEShort();
						final int first_x = buf.getShort2();
						for (int i = 0; i < steps; i++) {
					    	path[i][0] = buf.getByte2();
					    	path[i][1] = buf.getByte2();
						}
						final Movement movement = player$author.getMovement();
						movement.setRunningQueue(run_steps);
						movement.addStep(first_x, first_y);
						for (int i = 0; i < steps; i++) {
							path[i][0] += first_x;
					    	path[i][1] += first_y;
					    	movement.addStep(path[i][0], path[i][1]);
						}
						movement.finish();
					} break;
				}
				if (label != null) {
					eg.script.Scripts.invoke(player$author, label, arg_keys, arg_values);
				}
			} catch (Throwable throwable$) {
				throwable$.printStackTrace();
			}
			break;
		case "command":
			try {
				// commands.egs
				if (player$author.getPrivilege() == 2) {
					String[] args = args$.<String[]>get("args");
					switch (args$.<String>get("cmd")) {
						case "egsload": {
							player$author.message("Reloading scripts...");
							eg.script.Scripts.load();
						} break;
						case "test": {
							player$author.message("AAAaReloading scripts...9999");
						} break;
					}
				}
			} catch (Throwable throwable$) {
				throwable$.printStackTrace();
			}
			try {
				// commands.egs
				if (player$author.getPrivilege() == 1 || player$author.getPrivilege() == 2) {
					String[] args = args$.<String[]>get("args");
					switch (args$.<String>get("cmd")) {
						case "randomcmd": {
						} break;
					}
				}
			} catch (Throwable throwable$) {
				throwable$.printStackTrace();
			}
			break;
		}
	}
}
