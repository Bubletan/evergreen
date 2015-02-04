package eg.net.game.out.codec;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.MessagePacket;
import eg.util.io.Buffer;

public final class MessagePacketEncoder implements AbstractGamePacketEncoder<MessagePacket> {

	@Override
	public GamePacket encode(MessagePacket packet) throws Exception {
		String m = packet.getMessage();
		return new GamePacket(253, new Buffer(m.length() + 1).putLine(m).getData());
	}
}
