package eg.game.model.object;

/**
 * Represents the orientation of an {@link Object}.
 * 
 * @author Bubletan <https://github.com/Bubletan>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public enum ObjectOrientation {
    
    WEST(0), NORTH(1), EAST(2), SOUTH(3);
    
    private final int value;
    
    private ObjectOrientation(int value) {
        this.value = value;
    }
}
