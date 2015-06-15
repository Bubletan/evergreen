package eg.game.world;

public enum Direction {
    
    UNKNOWN, NORTH_WEST, NORTH, NORTH_EAST, WEST, EAST,
    SOUTH_WEST, SOUTH, SOUTH_EAST;
    
    public static Direction get(int dx, int dy) {
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
        return Direction.UNKNOWN;
    }
    
    public static boolean connectable(int dx, int dy) {
        return dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy);
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
            return UNKNOWN;
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
