package eg.net.game.in.codec;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.CameraAlteredPacket;
import eg.util.io.Buffer;

public final class CameraAlteredPacketDecoder implements AbstractGamePacketDecoder<CameraAlteredPacket> {

	@Override
	public CameraAlteredPacket decode(GamePacket packet) throws Exception {
		Buffer buf = packet.toBuffer();
		int roll = buf.getLEUShort();
		int yaw = buf.getLEUShort();
		return new CameraAlteredPacket(roll, yaw);
	}
}
