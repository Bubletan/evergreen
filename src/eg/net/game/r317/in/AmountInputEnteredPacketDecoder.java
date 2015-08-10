package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.AmountInputEnteredPacket;

public final class AmountInputEnteredPacketDecoder implements
        AbstractGamePacketDecoder<AmountInputEnteredPacket> {
    
    @Override
    public AmountInputEnteredPacket decode(GamePacket packet) throws Exception {
        return new AmountInputEnteredPacket(packet.toBuffer().getInt());
    }
}
