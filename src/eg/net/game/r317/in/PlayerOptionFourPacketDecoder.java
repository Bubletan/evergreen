package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.PlayerOptionFourPacket;

public final class PlayerOptionFourPacketDecoder implements AbstractGamePacketDecoder<PlayerOptionFourPacket> {
    
    @Override
    public PlayerOptionFourPacket decode(GamePacket packet) throws Exception {
        return new PlayerOptionFourPacket(packet.toBuffer().getLeUShort());
    }
}
