package eg.net.game.in.codec;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.WindowFocusAlteredPacket;

public final class WindowFocusAlteredPacketDecoder implements
		AbstractGamePacketDecoder<WindowFocusAlteredPacket> {
	
	@Override
	public WindowFocusAlteredPacket decode(GamePacket packet) throws Exception {
		return new WindowFocusAlteredPacket(packet.toBuffer().getBoolean());
	}
}
