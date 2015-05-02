package eg.script;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import eg.model.player.Player;

public final class Scripts {
	
	private static final String SCRIPT_GEN_CLASS_PATH = "eg.script.gen.ScriptGen";
	
	private static final ScriptClassLoader loader = new ScriptClassLoader();
	private static boolean reload;
	
	private static Class<?> scriptGenClass;
	private static Object scriptGenInstance;
	private static Method invokeMethod;

	private Scripts() {
	}
	
	public static final void load() {
		ScriptCompiler.getScriptCompiler().compile();
		if (scriptGenClass != null) {
			//TODO Compiling
		}
		boolean success = true;
		try {
			scriptGenClass = loader.loadClass(SCRIPT_GEN_CLASS_PATH);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			success = false;
		}
		try {
			scriptGenInstance = scriptGenClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			success = false;
		}
		try {
			invokeMethod = scriptGenClass.getMethod("invoke", Player.class, String.class, ScriptArguments.class);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			success = false;
		}
		if (!success) {
			scriptGenClass = null;
			scriptGenInstance = null;
			invokeMethod = null;
		}
	}
	
	public static final void invoke(Player author, String label, String[] keys, Object[] values) {
		if (invokeMethod != null) {
			try {
				invokeMethod.invoke(scriptGenInstance, author, label, new ScriptArguments(keys, values));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
