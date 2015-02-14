package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class CameraAlteredPacket implements AbstractGamePacket {
	
	private final int roll;
	private final int yaw;
	
	public CameraAlteredPacket(int roll, int yaw) {
		this.roll = roll;
		this.yaw = yaw;
	}
	
	public int getRoll() {
		return roll;
	}
	
	public int getYaw() {
		return yaw;
	}
}
