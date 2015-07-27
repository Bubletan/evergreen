package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.IntConfigPacket;
import eg.util.io.Buffer;

public final class IntConfigPacketEncoder implements AbstractGamePacketEncoder<IntConfigPacket> {
    
    @Override
    public GamePacket encode(IntConfigPacket packet) throws Exception {
        return new GamePacket(87, new Buffer(6).putLeShort(packet.getId())
                .putMeInt(packet.getValue()).toData());
    }
}
