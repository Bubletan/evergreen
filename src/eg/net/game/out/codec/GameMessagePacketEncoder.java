package eg.net.game.out.codec;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.GameMessagePacket;
import eg.util.io.Buffer;

public final class GameMessagePacketEncoder implements AbstractGamePacketEncoder<GameMessagePacket> {

	@Override
	public GamePacket encode(GameMessagePacket packet) throws Exception {
		String m = packet.getMessage();
		return new GamePacket(253, new Buffer(m.length() + 1).putLine(m).getData());
	}
}
