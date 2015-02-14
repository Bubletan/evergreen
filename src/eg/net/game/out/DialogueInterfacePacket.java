package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class DialogueInterfacePacket implements AbstractGamePacket {
	
	private final int id;
	
	public DialogueInterfacePacket(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
