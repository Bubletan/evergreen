package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.NpcOptionOnePacket;

public final class NpcOptionOnePacketDecoder implements AbstractGamePacketDecoder<NpcOptionOnePacket> {
    
    @Override
    public NpcOptionOnePacket decode(GamePacket packet) throws Exception {
        return new NpcOptionOnePacket(packet.toBuffer().getLeUShort());
    }
}
