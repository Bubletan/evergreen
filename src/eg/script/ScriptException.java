package eg.script;

public final class ScriptException extends Exception {
    
    private static final long serialVersionUID = 5750792330450905816L;
    
    private final Script script;
    
    public ScriptException(Script script, String message) {
        super(message);
        this.script = script;
    }
    
    public Script getScript() {
        return script;
    }
}
