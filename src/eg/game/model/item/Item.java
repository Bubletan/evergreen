package eg.game.model.item;

import com.google.common.base.Preconditions;

public final class Item {
    
    private final ItemType type;
    private final int quantity;
    
    public Item(ItemType type) {
        this(type, 1);
    }
    
    public Item(ItemType type, int quantity) {
        Preconditions.checkNotNull(type, "Type must not be null.");
        Preconditions.checkArgument(quantity > 0, "Quantity must be positive.");
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
