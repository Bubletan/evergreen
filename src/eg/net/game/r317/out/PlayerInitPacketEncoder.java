package eg.net.game.r317.out;

import eg.game.model.player.Player;
import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.PlayerInitPacket;
import eg.util.io.Buffer;

public final class PlayerInitPacketEncoder implements AbstractGamePacketEncoder<PlayerInitPacket> {
    
    @Override
    public GamePacket encode(PlayerInitPacket packet) throws Exception {
        Player player = packet.getPlayer();
        return new GamePacket(249, new Buffer(3).putAddedByte(player.isMember() ? 1 : 0)
                .putAddedLeShort(player.getIndex()).toData());
    }
}
