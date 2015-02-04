package eg.net.game.in.codec;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.MovementPacket;
import eg.util.io.Buffer;

public final class MovementPacketDecoder implements AbstractGamePacketDecoder<MovementPacket> {

	@Override
	public MovementPacket decode(GamePacket packet) throws Exception {
		int size = packet.getSize();
		if (packet.getType() == 248) {
			size -= 14;
		}
		int nPoints = (size - 5) >> 1;
		Buffer buf = packet.toBuffer();
		int x = buf.getLEShort2();
		byte[][] points = new byte[nPoints][2];
		for (int i = 0; i < nPoints; i++) {
			points[i][0] = buf.getByte();
			points[i][1] = buf.getByte();
		}
		int y = buf.getLEShort();
		boolean run = buf.getNegatedByte() == 1;
		return new MovementPacket(run, x, y, points);
	}
}
