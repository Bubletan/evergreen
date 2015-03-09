package eg.net.game.in.codec;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.KeepalivePacket;

public final class KeepalivePacketDecoder implements AbstractGamePacketDecoder<KeepalivePacket> {
    
    @Override
    public KeepalivePacket decode(GamePacket packet) throws Exception {
        return new KeepalivePacket(System.currentTimeMillis());
    }
}
