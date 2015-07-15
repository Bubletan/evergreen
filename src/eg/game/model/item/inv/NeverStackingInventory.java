package eg.game.model.item.inv;

import eg.game.model.item.ItemType;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class NeverStackingInventory extends AbstractInventory {
    
    public NeverStackingInventory(int space) {
        super(space);
    }
    
    @Override
    protected boolean isStackable(ItemType type) {
        return false;
    }
}
