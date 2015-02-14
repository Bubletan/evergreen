package eg.net.game.in.codec;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.MouseClickPacket;

public final class MouseClickPacketDecoder implements AbstractGamePacketDecoder<MouseClickPacket> {
	
	@Override
	public MouseClickPacket decode(GamePacket packet) throws Exception {
		int value = packet.toBuffer().getInt();
		
		long interval = (value >> 20) * 50;
		int button = (value >> 19) & 1;
		
		int coord = value & 0x3ffff;
        int x = coord % 765;
        int y = coord / 765;
		
		return new MouseClickPacket(interval, button, x, y);
	}
}
