package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.TabSelectPacket;
import eg.util.io.Buffer;

public final class TabSelectPacketEncoder implements AbstractGamePacketEncoder<TabSelectPacket> {
    
    @Override
    public GamePacket encode(TabSelectPacket packet) throws Exception {
        return new GamePacket(106, new Buffer(1).putNegatedByte(packet.getTab()).toData());
    }
}
