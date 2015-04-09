package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.SidebarInterfacePacket;
import eg.util.io.Buffer;

public final class SidebarInterfacePacketEncoder implements AbstractGamePacketEncoder<SidebarInterfacePacket> {
    
    @Override
    public GamePacket encode(SidebarInterfacePacket packet) throws Exception {
        return new GamePacket(71, new Buffer(3).putShort(packet.getInterfaceId())
                .putAddedByte(packet.getIndex()).toData());
    }
}
