package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.WalkableGameInterfacePacket;
import eg.util.io.Buffer;

public final class WalkableGameInterfacePacketEncoder implements
        AbstractGamePacketEncoder<WalkableGameInterfacePacket> {
    
    @Override
    public GamePacket encode(WalkableGameInterfacePacket packet) throws Exception {
        return new GamePacket(208, new Buffer(2).putLeShort(packet.getId()).toData());
    }
}
