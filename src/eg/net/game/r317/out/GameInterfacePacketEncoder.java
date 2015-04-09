package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.GameInterfacePacket;
import eg.util.io.Buffer;

public final class GameInterfacePacketEncoder implements
        AbstractGamePacketEncoder<GameInterfacePacket> {
    
    @Override
    public GamePacket encode(GameInterfacePacket packet) throws Exception {
        return new GamePacket(97, new Buffer(2).putShort(packet.getId())
                .toData());
    }
}
