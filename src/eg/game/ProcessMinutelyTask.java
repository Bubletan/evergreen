package eg.game;

import eg.util.task.Task;

public final class ProcessMinutelyTask implements Task {
    
    @Override
    public void execute() {
        
        World.getWorld().getPlayerList().parallelStream().forEach(player -> {
        });
    }
}
