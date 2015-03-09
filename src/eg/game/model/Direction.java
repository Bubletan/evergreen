package eg.game.model;

public enum Direction {
    
    NONE(-1), NORTH_WEST(0), NORTH(1), NORTH_EAST(2), WEST(3), EAST(4),
    SOUTH_WEST(5), SOUTH(6), SOUTH_EAST(7);
    
    public static Direction forDeltas(int dx, int dy) {
        if (dy == 1) {
            if (dx == 1) {
                return Direction.NORTH_EAST;
            } else if (dx == 0) {
                return Direction.NORTH;
            }
            return Direction.NORTH_WEST;
        } else if (dy == -1) {
            if (dx == 1) {
                return Direction.SOUTH_EAST;
            } else if (dx == 0) {
                return Direction.SOUTH;
            }
            return Direction.SOUTH_WEST;
        } else {
            if (dx == 1) {
                return Direction.EAST;
            } else if (dx == -1) {
                return Direction.WEST;
            }
        }
        return Direction.NONE;
    }
    
    public static boolean isConnectable(int dx, int dy) {
        return dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy);
    }
    
    private final int value;
    
    private Direction(int value) {
        this.value = value;
    }
    
    public int toInt() {
        return value;
    }
    
    public Direction toOpposite() {
        switch (this) {
        case NORTH_WEST:
            return SOUTH_EAST;
        case NORTH:
            return SOUTH;
        case NORTH_EAST:
            return SOUTH_WEST;
        case WEST:
            return EAST;
        case EAST:
            return WEST;
        case SOUTH_WEST:
            return NORTH_EAST;
        case SOUTH:
            return NORTH;
        case SOUTH_EAST:
            return NORTH_WEST;
        default:
            return NONE;
        }
    }
    
    public int getDeltaX() {
        switch (this) {
        case NORTH_EAST:
        case EAST:
        case SOUTH_EAST:
            return 1;
        case NORTH_WEST:
        case WEST:
        case SOUTH_WEST:
            return -1;
        default:
            return 0;
        }
    }
    
    public int getDeltaY() {
        switch (this) {
        case NORTH_EAST:
        case NORTH:
        case NORTH_WEST:
            return 1;
        case SOUTH_EAST:
        case SOUTH:
        case SOUTH_WEST:
            return -1;
        default:
            return 0;
        }
    }
}
