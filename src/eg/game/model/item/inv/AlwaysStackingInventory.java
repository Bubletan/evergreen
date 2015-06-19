package eg.game.model.item.inv;

import eg.game.model.item.ItemType;

public final class AlwaysStackingInventory extends AbstractInventory {
    
    public AlwaysStackingInventory(int space) {
        super(space);
    }
    
    @Override
    protected boolean isStackable(ItemType type) {
        return true;
    }
}
