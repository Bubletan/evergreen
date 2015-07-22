package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.InterfaceOffsetPacket;
import eg.util.io.Buffer;

public final class InterfaceOffsetPacketEncoder implements AbstractGamePacketEncoder<InterfaceOffsetPacket> {
    
    @Override
    public GamePacket encode(InterfaceOffsetPacket packet) throws Exception {
        return new GamePacket(70, new Buffer(6)
                .putShort(packet.getX())
                .putLeShort(packet.getY())
                .putLeShort(packet.getId())
                .toData());
    }
}
