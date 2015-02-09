package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class GameMessagePacket implements AbstractGamePacket {
	
	private final String message;
	
	public GameMessagePacket(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
