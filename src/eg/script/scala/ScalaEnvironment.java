package eg.script.scala;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import eg.script.Script;
import eg.script.ScriptException;
import eg.script.ScriptEnvironment;

public final class ScalaEnvironment implements ScriptEnvironment {
    
    private final ScriptEngine engine;
    
    public ScalaEnvironment() {
        engine = new ScriptEngineManager().getEngineByName("scala");
        
        // allow using java classpath
        @SuppressWarnings("rawtypes")
        scala.collection.immutable.List nil = scala.collection.immutable.Nil$.MODULE$;
        @SuppressWarnings("unchecked")
        scala.collection.immutable.$colon$colon<String> vals = scala.collection.immutable.$colon$colon$.MODULE$.apply("true", nil);
        ((scala.tools.nsc.interpreter.IMain) engine).settings().usejavacp().tryToSet(vals);
        
        // use the same classloader
        ((scala.tools.nsc.interpreter.IMain) engine).settings().embeddedDefaults(getClass().getClassLoader());
        
        try {
            engine.eval("import " + getClass().getPackage().getName() + ".ScalaDependencies._");
            engine.eval("import scala.language.dynamics");
            engine.eval("import scala.language.experimental.macros");
        } catch (javax.script.ScriptException e) {
            throw new RuntimeException("Error importing Scala dependencies.");
        }
    }
    
    @Override
    public void runScript(Script script) throws ScriptException {
        try {
            engine.eval("{");
            engine.eval(new FileReader(script.getPath()));
            engine.eval("}");
        } catch (javax.script.ScriptException e) {
            throw new ScriptException(script, e.getMessage());
        } catch (FileNotFoundException e) {
            throw new ScriptException(script, "Error reading file.");
        }
    }
}
