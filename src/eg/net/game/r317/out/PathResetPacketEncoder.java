package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.PathResetPacket;

public final class PathResetPacketEncoder implements AbstractGamePacketEncoder<PathResetPacket> {
    
    @Override
    public GamePacket encode(PathResetPacket packet) throws Exception {
        return new GamePacket(78);
    }
}
