package eg.net.game.in.codec;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.ChatMessagePacket;
import eg.util.io.Buffer;

public final class ChatMessagePacketDecoder implements AbstractGamePacketDecoder<ChatMessagePacket> {
	
	@Override
	public ChatMessagePacket decode(GamePacket packet) throws Exception {
		Buffer buf = packet.toBuffer();
		int animationEffect = buf.getUByte2();
		int colorEffect = buf.getUByte2();
		int length = packet.getSize() - 2;
		byte[] encodedMessage = buf.getBytesReversely(null, 0, length);
		for (int i = 0; i < length; i++) {
			encodedMessage[i] = (byte) (encodedMessage[i] + 128);
		}
		return new ChatMessagePacket(colorEffect, animationEffect, encodedMessage);
	}
}
