package eg.model.item;

public final class Item {

	private ItemType type;
	private int quantity;
	
	private Item(ItemType type, int quantity) {
		this.type = type;
		this.quantity = quantity;
	}
	
	public ItemType getType() {
		return type;
	}
	
	public int getQuantity() {
		return quantity;
	}
}
