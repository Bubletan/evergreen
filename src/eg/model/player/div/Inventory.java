package eg.model.player.div;

import eg.model.item.ItemList;

public final class Inventory {
	
	private static final int SIZE = 28;
	
	private ItemList list = new ItemList(SIZE, ItemList.STACK_SELECTIVELY);
}
