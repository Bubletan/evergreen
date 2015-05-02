package eg.script;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ScriptClassLoader extends ClassLoader {
	
	private static final ClassLoader loader = ScriptClassLoader.class.getClassLoader();
	
	public ScriptClassLoader() {
		super(loader);
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (!name.startsWith("eg")) {
			return super.loadClass(name);
		}
		Class<?> clazz;
		try {
			clazz = Class.forName(name, false, null);
		} catch (ClassNotFoundException e) {
			return super.loadClass(name);
		}
		try {
			String fileName = clazz.getSimpleName() + ".class";
			InputStream input = clazz.getResourceAsStream(fileName);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();
			while (data != -1) {
				buffer.write(data);
				data = input.read();
			}
			input.close();
	        byte[] classData = buffer.toByteArray();
	        return super.defineClass(name, classData, 0, classData.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clazz;
	}
}
