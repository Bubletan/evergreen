package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.IgnoreRemovalPacket;

public final class IgnoreRemovalPacketDecoder implements AbstractGamePacketDecoder<IgnoreRemovalPacket> {
    
    @Override
    public IgnoreRemovalPacket decode(GamePacket packet) throws Exception {
        return new IgnoreRemovalPacket(packet.toBuffer().getLong());
    }
}
