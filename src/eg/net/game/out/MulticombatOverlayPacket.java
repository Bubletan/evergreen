package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class MulticombatOverlayPacket implements AbstractGamePacket {
	
	private final boolean enabled;
	
	public MulticombatOverlayPacket(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
}
