package eg.net.game.out;

import eg.game.model.player.Player;
import eg.net.game.AbstractGamePacket;

public final class PlayerInitPacket implements AbstractGamePacket {
    
    private final Player player;
    
    public PlayerInitPacket(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }
}
