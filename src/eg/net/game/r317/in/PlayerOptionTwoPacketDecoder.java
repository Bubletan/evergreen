package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.PlayerOptionTwoPacket;

public final class PlayerOptionTwoPacketDecoder implements AbstractGamePacketDecoder<PlayerOptionTwoPacket> {
    
    @Override
    public PlayerOptionTwoPacket decode(GamePacket packet) throws Exception {
        return new PlayerOptionTwoPacket(packet.toBuffer().getLeUShort());
    }
}
