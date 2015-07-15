package eg.game.model.player;

import eg.Config;
import eg.net.game.out.CameraResetPacket;
import eg.net.game.out.GameMessagePacket;
import eg.net.game.out.PlayerInitPacket;
import eg.net.game.out.TabInterfacePacket;
import eg.util.task.Task;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class PlayerInitTask implements Task {
    
    private final Player player;
    
    public PlayerInitTask(Player player) {
        this.player = player;
    }
    
    @Override
    public void execute() {
        
        player.getSession().send(new PlayerInitPacket(player));
        player.getSession().send(new CameraResetPacket());
        
        player.getSession().send(new GameMessagePacket(Config.WELCOMING_MESSAGE));
        
        int[] sidebarIf = {2423, 3917, 638, 3213, 1644, 5608, 1151, 2423, 5065, 5715, 2449, 4445, 147, 6299};
        for (int i = 0; i < sidebarIf.length; i++) {
            player.getSession().send(new TabInterfacePacket(i, sidebarIf[i]));
        }
    }
}
