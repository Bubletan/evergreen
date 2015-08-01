package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.NameInputInterfacePacket;

public final class NameInputInterfacePacketEncoder implements
        AbstractGamePacketEncoder<NameInputInterfacePacket> {
    
    @Override
    public GamePacket encode(NameInputInterfacePacket packet) throws Exception {
        return new GamePacket(187);
    }
}
