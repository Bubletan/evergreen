package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.SystemUpdatePacket;
import eg.util.io.Buffer;

public final class SystemUpdatePacketEncoder implements AbstractGamePacketEncoder<SystemUpdatePacket> {
    
    @Override
    public GamePacket encode(SystemUpdatePacket packet) throws Exception {
        return new GamePacket(114, new Buffer(2).putLeShort(packet.getSeconds()).toData());
    }
}
