package eg.net.game.out;

import eg.net.game.AbstractGamePacket;
import eg.util.io.Buffer;

public final class PlayerSyncPacket implements AbstractGamePacket {
	
	private final Buffer buffer;
	
	public PlayerSyncPacket(Buffer buffer) {
		this.buffer = buffer;
	}
	
	public Buffer getBuffer() {
		return buffer;
	}
}
