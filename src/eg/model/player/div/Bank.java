package eg.model.player.div;

import eg.model.item.ItemContainer;

public final class Bank {

	private static final int MAX_SIZE = 352;
	
	private ItemContainer list;
	private int count;

	public final int getCount() {
		return count;
	}
}
