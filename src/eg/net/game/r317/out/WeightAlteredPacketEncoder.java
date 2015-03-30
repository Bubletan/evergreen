package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.WeightAlteredPacket;
import eg.util.io.Buffer;

public final class WeightAlteredPacketEncoder implements AbstractGamePacketEncoder<WeightAlteredPacket> {
    
    @Override
    public GamePacket encode(WeightAlteredPacket packet) throws Exception {
        return new GamePacket(240, new Buffer(2).putShort(packet.getWeight()).getData());
    }
}
