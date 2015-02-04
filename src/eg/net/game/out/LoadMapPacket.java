package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class LoadMapPacket implements AbstractGamePacket {
	
	private final int x;
	private final int y;
	
	public LoadMapPacket(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
