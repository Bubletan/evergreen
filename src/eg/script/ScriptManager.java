package eg.script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import eg.script.scala.ScalaEnvironment;

public final class ScriptManager {
    
    private final Map<String, ScriptEnvironment> envForExtension = new HashMap<>();
    
    public ScriptManager() {
        envForExtension.put("scala", new ScalaEnvironment());
    }
    
    public void loadScripts(String directory) {
        try {
            Files.walk(Paths.get(directory))
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .forEach(this::loadScript);
        } catch (IOException e) {
            throw new RuntimeException("Error loading scripts.");
        }
    }
    
    private void loadScript(String path) {
        int i = path.lastIndexOf('.');
        if (i != -1) {
            String extension = path.substring(i + 1);
            ScriptEnvironment env = envForExtension.get(extension);
            if (env != null) {
                try {
                    env.runScript(new Script(path));
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
