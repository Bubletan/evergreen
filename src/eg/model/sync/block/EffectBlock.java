package eg.model.sync.block;

import eg.model.req.Effect;
import eg.model.sync.SyncBlock;

public final class EffectBlock extends SyncBlock {
	
	private final Effect effect;
	
	public EffectBlock(Effect effect) {
		this.effect = effect;
	}
	
	public Effect getEffect() {
		return effect;
	}
}
