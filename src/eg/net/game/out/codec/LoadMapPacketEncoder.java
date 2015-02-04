package eg.net.game.out.codec;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.LoadMapPacket;
import eg.util.io.Buffer;

public final class LoadMapPacketEncoder implements AbstractGamePacketEncoder<LoadMapPacket> {
	
	@Override
	public GamePacket encode(LoadMapPacket packet) throws Exception {
		return new GamePacket(73, new Buffer(4)
				.put128PlusShort(packet.getX())
				.putShort(packet.getY())
				.getData());
	}
}
