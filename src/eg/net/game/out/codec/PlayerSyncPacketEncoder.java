package eg.net.game.out.codec;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.PlayerSyncPacket;
import eg.util.io.Buffer;

public final class PlayerSyncPacketEncoder implements
		AbstractGamePacketEncoder<PlayerSyncPacket> {
	
	@Override
	public GamePacket encode(PlayerSyncPacket packet) throws Exception {
		Buffer buf = packet.getBuffer();
		return new GamePacket(81, buf.getData(), buf.getPosition());
	}
}
