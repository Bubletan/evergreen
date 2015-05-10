package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.NpcOptionThreePacket;

public final class NpcOptionThreePacketDecoder implements AbstractGamePacketDecoder<NpcOptionThreePacket> {
    
    @Override
    public NpcOptionThreePacket decode(GamePacket packet) throws Exception {
        return new NpcOptionThreePacket(packet.toBuffer().getUShort());
    }
}
