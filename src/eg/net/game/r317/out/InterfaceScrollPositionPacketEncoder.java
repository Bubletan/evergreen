package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.InterfaceScrollPositionPacket;
import eg.util.io.Buffer;

public final class InterfaceScrollPositionPacketEncoder implements
        AbstractGamePacketEncoder<InterfaceScrollPositionPacket> {
    
    @Override
    public GamePacket encode(InterfaceScrollPositionPacket packet)  throws Exception {
        return new GamePacket(79, new Buffer(4).putLeShort(packet.getId())
                .putAddedShort(packet.getPosition()).toData());
    }
}
