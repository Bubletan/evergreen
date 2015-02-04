package eg.model.item;

public final class ItemList {
	
	public static final int STACK_SELECTIVELY = 0;
	public static final int STACK_ALWAYS = 1;
	
	private int size;
	private Item[] items;
	private int stack;
	
	public ItemList(int size, int stack) {
		if (stack != STACK_SELECTIVELY && stack != STACK_ALWAYS) {
			throw new IllegalArgumentException("Stack must be 0 or 1.");
		}
		this.size = size;
		items = new Item[size];
		this.stack = stack;
	}
	
	public boolean contains(ItemType type) {
		for (int i = 0; i < size; i++) {
			if (items[i].getType() == type) {
				return true;
			}
		}
		return false;
	}
	
	public void set(int index, Item item) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("Index out of bounds.");
		} else {
			items[index] = item;
		}
	}
	
	public Item get(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("Index out of bounds.");
		} else {
			return items[index];
		}
	}
}
