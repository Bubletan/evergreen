package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class WeightUpdatePacket implements AbstractGamePacket {
	
	private final int weight;
	
	public WeightUpdatePacket(int weight) {
		this.weight = weight;
	}
	
	public int getWeight() {
		return weight;
	}
}
