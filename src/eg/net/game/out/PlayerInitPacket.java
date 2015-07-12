package eg.net.game.out;

import java.util.Objects;

import eg.game.model.player.Player;
import eg.net.game.AbstractGamePacket;

public final class PlayerInitPacket implements AbstractGamePacket {
    
    private final Player player;
    
    public PlayerInitPacket(Player player) {
        this.player = Objects.requireNonNull(player);
    }
    
    public Player getPlayer() {
        return player;
    }
}
