package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.TabFlashPacket;
import eg.util.io.Buffer;

public final class TabFlashPacketEncoder implements AbstractGamePacketEncoder<TabFlashPacket> {
    
    @Override
    public GamePacket encode(TabFlashPacket packet) throws Exception {
        return new GamePacket(24, new Buffer(1).putSubtractedByte(packet.getTab()).toData());
    }
}
