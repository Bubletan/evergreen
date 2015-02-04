package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class SidebarInterfacePacket implements AbstractGamePacket {
	
	private final int index;
	private final int interfaceId;
	
	public SidebarInterfacePacket(int index, int interfaceId) {
		this.index = index;
		this.interfaceId = interfaceId;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getInterfaceId() {
		return interfaceId;
	}
}
