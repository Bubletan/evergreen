package eg.model.sync.block;

import eg.model.player.Player;
import eg.model.sync.SyncBlock;

//TODO: Change this (temporary solution)
public final class AppearanceBlock extends SyncBlock {
	
	private final Player player;
	
	public AppearanceBlock(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
}
