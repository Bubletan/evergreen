package eg.net.game.out.codec;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.InterfaceTextPacket;
import eg.util.io.Buffer;

public final class InterfaceTextPacketEncoder implements AbstractGamePacketEncoder<InterfaceTextPacket> {
	
	@Override
	public GamePacket encode(InterfaceTextPacket packet) throws Exception {
		String text = packet.getText();
		return new GamePacket(126, new Buffer(text.length() + 3)
			.putLine(text).put128PlusShort(packet.getId()).getData());
	}
}
