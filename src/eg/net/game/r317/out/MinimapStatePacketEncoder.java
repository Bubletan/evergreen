package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.MinimapStatePacket;
import eg.util.io.Buffer;

public final class MinimapStatePacketEncoder implements AbstractGamePacketEncoder<MinimapStatePacket> {
    
    @Override
    public GamePacket encode(MinimapStatePacket packet) throws Exception {
        int state;
        switch (packet.getState()) {
        case DEFAULT:
            state = 0;
            break;
        case UNWALKABLE:
            state = 1;
            break;
        case HIDDEN:
            state = 2;
            break;
        default:
            throw new IllegalArgumentException("Unknown state.");
        }
        return new GamePacket(99, new Buffer(1).putByte(state).toData());
    }
}
