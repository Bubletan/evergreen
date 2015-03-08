package eg.game.model.player;

import eg.game.model.item.ItemContainer;

public final class Bank {

	private static final int MAX_SIZE = 352;
	
	private ItemContainer list;
	private int count;

	public final int getCount() {
		return count;
	}
}
