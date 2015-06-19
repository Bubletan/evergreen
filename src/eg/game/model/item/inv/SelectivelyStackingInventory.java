package eg.game.model.item.inv;

import eg.game.model.item.ItemType;

public final class SelectivelyStackingInventory extends AbstractInventory {
    
    public SelectivelyStackingInventory(int space) {
        super(space);
    }
    
    @Override
    protected boolean isStackable(ItemType type) {
        return type.isStackable();
    }
}
