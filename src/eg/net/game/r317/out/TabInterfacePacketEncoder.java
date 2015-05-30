package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.TabInterfacePacket;
import eg.util.io.Buffer;

public final class TabInterfacePacketEncoder implements AbstractGamePacketEncoder<TabInterfacePacket> {
    
    @Override
    public GamePacket encode(TabInterfacePacket packet) throws Exception {
        return new GamePacket(71, new Buffer(3).putShort(packet.getId())
                .putAddedByte(packet.getTab()).toData());
    }
}
