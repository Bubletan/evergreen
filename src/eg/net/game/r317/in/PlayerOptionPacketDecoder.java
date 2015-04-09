package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.PlayerOptionPacket;
import eg.util.io.Buffer;

public final class PlayerOptionPacketDecoder implements AbstractGamePacketDecoder<PlayerOptionPacket> {
    
    private static final int OPTION_0 = 128;
    private static final int OPTION_1 = 153;
    private static final int OPTION_2 = 73;
    private static final int OPTION_3 = 139;
    private static final int OPTION_4 = 39;
    
    @Override
    public PlayerOptionPacket decode(GamePacket packet) throws Exception {
        Buffer buf = packet.toBuffer();
        int op, idx;
        
        switch (packet.getType()) {
        
        case OPTION_0:
            op = 0;
            idx = buf.getUShort();
            break;
            
        case OPTION_1:
            op = 1;
            idx = buf.getLeUShort();
            break;
            
        case OPTION_2:
            op = 2;
            idx = buf.getLeUShort();
            break;
            
        case OPTION_3:
            op = 3;
            idx = buf.getLeUShort();
            break;
            
        case OPTION_4:
            op = 4;
            idx = buf.getLeUShort();
            break;
            
        default:
            throw new Exception();
        }
        
        return new PlayerOptionPacket(op, idx);
    }
}
