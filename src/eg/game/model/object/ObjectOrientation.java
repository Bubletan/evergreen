package eg.game.model.object;

/**
 * @author Bubletan <https://github.com/Bubletan>
 * @author lare96 <http://github.com/lare96>
 */
public enum ObjectOrientation {
    
    WEST(0), NORTH(1), EAST(2), SOUTH(3);
    
    private final int value;
    
    private ObjectOrientation(int value) {
        this.value = value;
    }
}
