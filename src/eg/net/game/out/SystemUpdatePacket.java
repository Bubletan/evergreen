package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class SystemUpdatePacket implements AbstractGamePacket {
	
	private final int seconds;
	
	public SystemUpdatePacket(int seconds) {
		this.seconds = seconds;
	}
	
	public int getSeconds() {
		return seconds;
	}
}
