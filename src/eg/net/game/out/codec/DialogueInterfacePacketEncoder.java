package eg.net.game.out.codec;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.DialogueInterfacePacket;
import eg.util.io.Buffer;

public final class DialogueInterfacePacketEncoder implements
		AbstractGamePacketEncoder<DialogueInterfacePacket> {
	
	@Override
	public GamePacket encode(DialogueInterfacePacket packet) throws Exception {
		return new GamePacket(164, new Buffer(2).putLEShort(packet.getId()).getData());
	}
}
