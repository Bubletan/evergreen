package eg.model.sync.block;

import eg.model.req.Hit;
import eg.model.sync.SyncBlock;

public final class PrimaryHitBlock extends SyncBlock {
	
	private final Hit hit;
	private final int leftHealth;
	private final int totalHealth;
	
	public PrimaryHitBlock(Hit hit, int leftHealth, int totalHealth) {
		this.hit = hit;
		this.leftHealth = leftHealth;
		this.totalHealth = totalHealth;
	}
	
	public Hit getHit() {
		return hit;
	}
	
	public int getLeftHealth() {
		return leftHealth;
	}
	
	public int getTotalHealth() {
		return totalHealth;
	}
}
