package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.NpcOptionTwoPacket;

public final class NpcOptionTwoPacketDecoder implements AbstractGamePacketDecoder<NpcOptionTwoPacket> {
    
    @Override
    public NpcOptionTwoPacket decode(GamePacket packet) throws Exception {
        return new NpcOptionTwoPacket(packet.toBuffer().getAddedLeUShort());
    }
}
