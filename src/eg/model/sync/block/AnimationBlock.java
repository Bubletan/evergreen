package eg.model.sync.block;

import eg.model.req.Animation;
import eg.model.sync.SyncBlock;

public final class AnimationBlock extends SyncBlock {
	
	private final Animation animation;
	
	public AnimationBlock(Animation animation) {
		this.animation = animation;
	}
	
	public Animation getAnimation() {
		return animation;
	}
}
