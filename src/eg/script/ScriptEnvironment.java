package eg.script;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public interface ScriptEnvironment {
    
    public void runScript(Script script) throws ScriptException;
}
