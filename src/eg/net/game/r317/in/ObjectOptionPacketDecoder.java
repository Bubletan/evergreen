package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.ObjectOptionPacket;
import eg.util.io.Buffer;

public final class ObjectOptionPacketDecoder implements AbstractGamePacketDecoder<ObjectOptionPacket> {
    
    private static final int OPTION_0 = 132;
    private static final int OPTION_1 = 252;
    private static final int OPTION_2 = 70;
    
    @Override
    public ObjectOptionPacket decode(GamePacket packet) throws Exception {
        Buffer buf = packet.toBuffer();
        int op, id, x, y;
        
        switch (packet.getType()) {
        
        case OPTION_0:
            op = 0;
            x = buf.getAddedLeUShort();
            id = buf.getUShort();
            y = buf.getAddedUShort();
            break;
            
        case OPTION_1:
            op = 1;
            id = buf.getAddedLeUShort();
            y = buf.getLeUShort();
            x = buf.getAddedUShort();
            break;
            
        case OPTION_2:
            op = 2;
            x = buf.getLeUShort();
            y = buf.getUShort();
            id = buf.getAddedLeUShort();
            break;
            
        default:
            throw new Exception();
        }
        
        return new ObjectOptionPacket(op, id, x, y);
    }
}
 