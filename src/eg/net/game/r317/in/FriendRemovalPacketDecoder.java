package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.FriendRemovalPacket;

public final class FriendRemovalPacketDecoder implements AbstractGamePacketDecoder<FriendRemovalPacket> {
    
    @Override
    public FriendRemovalPacket decode(GamePacket packet) throws Exception {
        return new FriendRemovalPacket(packet.toBuffer().getLong());
    }
}
