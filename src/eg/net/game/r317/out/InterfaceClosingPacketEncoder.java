package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.InterfaceClosingPacket;

public final class InterfaceClosingPacketEncoder implements AbstractGamePacketEncoder<InterfaceClosingPacket> {
    
    @Override
    public GamePacket encode(InterfaceClosingPacket packet) throws Exception {
        return new GamePacket(219);
    }
}
