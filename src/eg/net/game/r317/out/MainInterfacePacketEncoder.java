package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.MainInterfacePacket;
import eg.util.io.Buffer;

public final class MainInterfacePacketEncoder implements
        AbstractGamePacketEncoder<MainInterfacePacket> {
    
    @Override
    public GamePacket encode(MainInterfacePacket packet) throws Exception {
        return new GamePacket(97, new Buffer(2).putShort(packet.getId())
                .getData());
    }
}
