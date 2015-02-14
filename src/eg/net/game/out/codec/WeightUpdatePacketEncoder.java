package eg.net.game.out.codec;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.WeightUpdatePacket;
import eg.util.io.Buffer;

public final class WeightUpdatePacketEncoder implements AbstractGamePacketEncoder<WeightUpdatePacket> {
	
	@Override
	public GamePacket encode(WeightUpdatePacket packet) throws Exception {
		return new GamePacket(240, new Buffer(2).putShort(packet.getWeight()).getData());
	}
}
