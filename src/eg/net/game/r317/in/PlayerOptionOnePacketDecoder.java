package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.PlayerOptionOnePacket;

public final class PlayerOptionOnePacketDecoder implements AbstractGamePacketDecoder<PlayerOptionOnePacket> {
    
    @Override
    public PlayerOptionOnePacket decode(GamePacket packet) throws Exception {
        return new PlayerOptionOnePacket(packet.toBuffer().getUShort());
    }
}
