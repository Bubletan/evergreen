package eg.game.world;

/**
 * @author Bubletan <https://github.com/Bubletan>
 * @author Graham
 */
public enum Direction {
    
    UNKNOWN(0, 0),
    NORTH_WEST(-1, 1),
    NORTH(0, 1),
    NORTH_EAST(1, 1),
    WEST(-1, 0),
    EAST(1, 0),
    SOUTH_WEST(-1, -1),
    SOUTH(0, -1),
    SOUTH_EAST(1, -1);
    
    private final int dx;
    private final int dy;
    
    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public static Direction get(int dx, int dy) {
        if (dy == 1) {
            if (dx == 1) {
                return NORTH_EAST;
            }
            if (dx == 0) {
                return NORTH;
            }
            if (dx == -1) {
                return NORTH_WEST;
            }
        }
        if (dy == 0) {
            if (dx == 1) {
                return EAST;
            }
            if (dx == -1) {
                return WEST;
            }
        }
        if (dy == -1) {
            if (dx == 1) {
                return SOUTH_EAST;
            }
            if (dx == 0) {
                return SOUTH;
            }
            if (dx == -1) {
                return SOUTH_WEST;
            }
        }
        return UNKNOWN;
    }
    
    public static boolean connectable(int dx, int dy) {
        return dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy);
    }
    
    public int getDeltaX() {
        return dx;
    }
    
    public int getDeltaY() {
        return dy;
    }
    
    public Direction toOpposite() {
        return get(-dx, -dy);
    }
}
