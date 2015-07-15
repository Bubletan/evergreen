package eg.game;

import eg.Config;
import eg.net.GameServer;
import eg.script.ScriptManager;
import eg.util.task.Task;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class StartTask implements Task {
    
    @Override
    public void execute() {
        
        new ScriptManager().loadScripts("./script/");
        
        new GameServer().bind(Config.PORT);
    }
}
