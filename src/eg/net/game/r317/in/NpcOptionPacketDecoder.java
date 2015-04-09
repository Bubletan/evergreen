package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.NpcOptionPacket;
import eg.util.io.Buffer;

public final class NpcOptionPacketDecoder implements AbstractGamePacketDecoder<NpcOptionPacket> {
    
    private static final int OPTION_0 = 155;
    private static final int OPTION_1 = 17;
    private static final int OPTION_2 = 21;
    
    @Override
    public NpcOptionPacket decode(GamePacket packet) throws Exception {
        Buffer buf = packet.toBuffer();
        int op, idx;
        
        switch (packet.getType()) {
        
        case OPTION_0:
            op = 0;
            idx = buf.getLeUShort();
            break;
            
        case OPTION_1:
            op = 1;
            idx = buf.getAddedLeUShort();
            break;
            
        case OPTION_2:
            op = 2;
            idx = buf.getUShort();
            break;
            
        default:
            throw new Exception();
        }
        
        return new NpcOptionPacket(op, idx);
    }
}
