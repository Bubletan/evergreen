package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.AmountInputInterfacePacket;

public final class AmountInputInterfacePacketEncoder implements
        AbstractGamePacketEncoder<AmountInputInterfacePacket> {
    
    @Override
    public GamePacket encode(AmountInputInterfacePacket packet) throws Exception {
        return new GamePacket(27);
    }
}
