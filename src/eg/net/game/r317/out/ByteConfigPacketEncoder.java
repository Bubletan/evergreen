package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.ByteConfigPacket;
import eg.util.io.Buffer;

public final class ByteConfigPacketEncoder implements AbstractGamePacketEncoder<ByteConfigPacket> {
    
    @Override
    public GamePacket encode(ByteConfigPacket packet) throws Exception {
        return new GamePacket(36, new Buffer(3).putLeShort(packet.getId())
                .putByte(packet.getValue()).toData());
    }
}
