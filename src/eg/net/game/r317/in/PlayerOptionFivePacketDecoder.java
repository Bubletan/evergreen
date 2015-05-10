package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.PlayerOptionFivePacket;

public final class PlayerOptionFivePacketDecoder implements AbstractGamePacketDecoder<PlayerOptionFivePacket> {
    
    @Override
    public PlayerOptionFivePacket decode(GamePacket packet) throws Exception {
        return new PlayerOptionFivePacket(packet.toBuffer().getLeUShort());
    }
}
