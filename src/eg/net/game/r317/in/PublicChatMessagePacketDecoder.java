package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.PublicChatMessagePacket;
import eg.util.io.Buffer;

public final class PublicChatMessagePacketDecoder implements AbstractGamePacketDecoder<PublicChatMessagePacket> {
    
    @Override
    public PublicChatMessagePacket decode(GamePacket packet) throws Exception {
        Buffer buf = packet.toBuffer();
        int animationEffect = buf.getSubtractedUByte();
        int colorEffect = buf.getSubtractedUByte();
        byte[] encodedMessage = buf.getAddedBytesReversely(null, 0, packet.getSize() - 2);
        return new PublicChatMessagePacket(colorEffect, animationEffect, encodedMessage);
    }
}
