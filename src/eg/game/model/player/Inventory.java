package eg.game.model.player;

import eg.game.model.item.ItemContainer;

public final class Inventory {
    
    private static final int SIZE = 28;
    
    private ItemContainer list = new ItemContainer(SIZE, ItemContainer.Type.STACK_SELECTIVELY);
}
