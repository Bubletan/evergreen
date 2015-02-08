package eg.model.item;

import com.google.common.base.Preconditions;

public final class ItemContainer {
	
	public static final int STACK_SELECTIVELY = 0;
	public static final int STACK_ALWAYS = 1;
	public static final int STACK_NEVER = 2;
	
	private int capacity;
	private int size;
	private Item[] items;
	private int stack;
	
	public ItemContainer(int capacity, int stack) {
		Preconditions.checkArgument(stack >= 0 && stack <= 2, "Illegal stack value.");
		this.capacity = capacity;
		items = new Item[size];
		this.stack = stack;
	}
	
	public boolean contains(Item item) {
		if (item == Item.NOTHING) {
			return size != capacity;
		} else if (size == 0) {
			return false;
		}
		ItemType type = item.getType();
		int quantity = item.getQuantity();
		if (stack == STACK_ALWAYS || stack == STACK_SELECTIVELY && type.isStackable()) {
			int used = 0;
			for (Item it : items) {
				if (it != null) {
					if (it.getType() == type) {
						return it.getQuantity() >= quantity;
					}
					if (++used == size) {
						break;
					}
				}
			}
		} else {
			int count = 0;
			int used = 0;
			for (Item it : items) {  
				if (it != null) {
					if (it.getType() == type && ++count >= quantity) {
						return true;
					}
					if (++used == size) {
						break;
					}
				}
			}
		}
		return false;
	}
	
	public Item remove(Item item) {
		if (size == 0 || item == Item.NOTHING) {
			return Item.NOTHING;
		}
		ItemType type = item.getType();
		int quantity = item.getQuantity();
		if (stack == STACK_ALWAYS || stack == STACK_SELECTIVELY && type.isStackable()) {
			int used = 0;
			for (int i = 0;; i++) {
				Item it = items[i];
				if (it != null) {
					if (it.getType() == type) {
						int qu = it.getQuantity();
						if (qu > quantity) {
							items[i] = new Item(type, qu - quantity);
							return item;
						} else {
							items[i] = null;
							size--;
							return it;
						}
					}
					if (++used == size) {
						break;
					}
				}
			}
			return Item.NOTHING;
		} else {
			int count = 0;
			int used = 0;
			for (int i = 0;; i++) {
				Item it = items[i];
				if (it != null) {
					if (it.getType() == type) {
						items[i] = null;
						size--;
						if (++count == quantity) {
							return item;
						}
					}
					if (++used == size) {
						break;
					}
				}
			}
			if (count == 0) {
				return Item.NOTHING;
			}
			return new Item(type, count);
		}
	}
	
	public Item add(Item item) {
		if (item == Item.NOTHING) {
			return item;
		}
		ItemType type = item.getType();
		int quantity = item.getQuantity();
		if (stack == STACK_ALWAYS || stack == STACK_SELECTIVELY && type.isStackable()) {
			int used = 0;
			for (int i = 0;; i++) {
				Item it = items[i];
				if (it != null) {
					if (it.getType() == type) {
						int qu = it.getQuantity();
						if (qu == Integer.MAX_VALUE) {
							return item;
						} else if (Integer.MAX_VALUE - qu >= quantity) {
							items[i] = new Item(type, qu + quantity);
							return Item.NOTHING;
						} else {
							items[i] = new Item(type, Integer.MAX_VALUE);
							return new Item(type, quantity - (Integer.MAX_VALUE - qu));
						}
					}
					if (++used == size) {
						break;
					}
				}
			}
			for (int i = 0; i < capacity; i++) {
				if (items[i] == null) {
					items[i] = item;
					size++;
					return Item.NOTHING;
				}
			}
			return item;
		} else {
			if (size == capacity) {
				return item;
			}
			Item one = null;
			int count = 0;
			for (int i = 0; i < capacity; i++) {
				Item it = items[i];
				if (it == null) {
					if (one == null) {
						one = new Item(type);
					}
					items[i] = one;
					size++;
					if (++count == quantity) {
						return Item.NOTHING;
					}
				} else if (one == null && it.getType() == type) {
					one = it;
				}
			}
			return new Item(type, quantity - count);
		}
	}
	
	public void swap(int slot1, int slot2) {
		Preconditions.checkElementIndex(slot1, capacity, "Slot one out of bounds: " + slot1);
		Preconditions.checkElementIndex(slot2, capacity, "Slot two out of bounds: " + slot2);
		if (slot1 != slot2) {
			Item tmp = items[slot1];
			items[slot1] = items[slot2];
			items[slot2] = tmp;
		}
	}
	
	public void insert(int slot1, int slot2) {
		Preconditions.checkElementIndex(slot1, capacity, "Slot one out of bounds: " + slot1);
		Preconditions.checkElementIndex(slot2, capacity, "Slot two out of bounds: " + slot2);
		if (slot2 > slot1) {
			for (int i = slot1; i < slot2; i++) {
				int i1 = i + 1;
				Item tmp = items[i];
				items[i] = items[i1];
				items[i1] = tmp;
			}
		} else if (slot1 > slot2) {
			for (int i = slot1; i > slot2; i--) {
				int i1 = i - 1;
				Item tmp = items[i];
				items[i] = items[i1];
				items[i1] = tmp;
			}
		}
	}
}
