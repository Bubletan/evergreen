package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.PrivateChatMessagePacket;
import eg.util.io.Buffer;

public final class PrivateChatMessagePacketDecoder implements AbstractGamePacketDecoder<PrivateChatMessagePacket> {
    
    @Override
    public PrivateChatMessagePacket decode(GamePacket packet) throws Exception {
        Buffer buf = packet.toBuffer();
        long recipient = buf.getLong();
        byte[] encodedMessage = buf.getBytes(null, 0, packet.getSize() - 8);
        return new PrivateChatMessagePacket(recipient, encodedMessage);
    }
}
