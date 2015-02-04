package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class MessagePacket implements AbstractGamePacket {
	
	private final String message;
	
	public MessagePacket(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
