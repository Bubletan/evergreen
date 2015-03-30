package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.LogoutPacket;

public final class LogoutPacketEncoder implements AbstractGamePacketEncoder<LogoutPacket> {
    
    @Override
    public GamePacket encode(LogoutPacket packet) throws Exception {
        return new GamePacket(109);
    }
}
