package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.ObjectOptionOnePacket;
import eg.util.io.Buffer;

public final class ObjectOptionOnePacketDecoder implements AbstractGamePacketDecoder<ObjectOptionOnePacket> {
    
    @Override
    public ObjectOptionOnePacket decode(GamePacket packet) throws Exception {
        Buffer buf = packet.toBuffer();
        int x = buf.getAddedLeUShort();
        int id = buf.getUShort();
        int y = buf.getAddedUShort();
        return new ObjectOptionOnePacket(id, x, y);
    }
}
