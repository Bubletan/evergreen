package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.FriendAdditionPacket;

public final class FriendAdditionPacketDecoder implements AbstractGamePacketDecoder<FriendAdditionPacket> {
    
    @Override
    public FriendAdditionPacket decode(GamePacket packet) throws Exception {
        return new FriendAdditionPacket(packet.toBuffer().getLong());
    }
}
