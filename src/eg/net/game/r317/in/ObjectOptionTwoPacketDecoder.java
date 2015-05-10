package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.ObjectOptionTwoPacket;
import eg.util.io.Buffer;

public final class ObjectOptionTwoPacketDecoder implements AbstractGamePacketDecoder<ObjectOptionTwoPacket> {
    
    @Override
    public ObjectOptionTwoPacket decode(GamePacket packet) throws Exception {
        Buffer buf = packet.toBuffer();
        int id = buf.getAddedLeUShort();
        int y = buf.getLeUShort();
        int x = buf.getAddedUShort();
        return new ObjectOptionTwoPacket(id, x, y);
    }
}
