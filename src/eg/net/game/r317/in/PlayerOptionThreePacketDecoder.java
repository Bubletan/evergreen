package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.PlayerOptionThreePacket;

public final class PlayerOptionThreePacketDecoder implements AbstractGamePacketDecoder<PlayerOptionThreePacket> {
    
    @Override
    public PlayerOptionThreePacket decode(GamePacket packet) throws Exception {
        return new PlayerOptionThreePacket(packet.toBuffer().getLeUShort());
    }
}
