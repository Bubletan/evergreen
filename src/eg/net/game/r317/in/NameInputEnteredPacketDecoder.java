package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.NameInputEnteredPacket;

public final class NameInputEnteredPacketDecoder implements
        AbstractGamePacketDecoder<NameInputEnteredPacket> {
    
    @Override
    public NameInputEnteredPacket decode(GamePacket packet) throws Exception {
        return new NameInputEnteredPacket(packet.toBuffer().getLong());
    }
}
