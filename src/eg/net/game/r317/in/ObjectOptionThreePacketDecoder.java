package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.ObjectOptionThreePacket;
import eg.util.io.Buffer;

public final class ObjectOptionThreePacketDecoder implements AbstractGamePacketDecoder<ObjectOptionThreePacket> {
    
    @Override
    public ObjectOptionThreePacket decode(GamePacket packet) throws Exception {
        Buffer buf = packet.toBuffer();
        int x = buf.getLeUShort();
        int y = buf.getUShort();
        int id = buf.getAddedLeUShort();
        return new ObjectOptionThreePacket(id, x, y);
    }
}
