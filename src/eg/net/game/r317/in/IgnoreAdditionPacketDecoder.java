package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.IgnoreAdditionPacket;

public final class IgnoreAdditionPacketDecoder implements AbstractGamePacketDecoder<IgnoreAdditionPacket> {
    
    @Override
    public IgnoreAdditionPacket decode(GamePacket packet) throws Exception {
        return new IgnoreAdditionPacket(packet.toBuffer().getLong());
    }
}
