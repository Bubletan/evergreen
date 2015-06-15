package eg.game.world.sync;

import java.util.EnumMap;
import java.util.Map;

import eg.game.world.Direction;

public final class SyncUtils {
    
    private static final Map<Direction, Integer> directionToIntMap = new EnumMap<>(Direction.class);
    
    static {
        directionToIntMap.put(Direction.NORTH_WEST, 0);
        directionToIntMap.put(Direction.NORTH, 1);
        directionToIntMap.put(Direction.NORTH_EAST, 2);
        directionToIntMap.put(Direction.WEST, 3);
        directionToIntMap.put(Direction.EAST, 4);
        directionToIntMap.put(Direction.SOUTH_WEST, 5);
        directionToIntMap.put(Direction.SOUTH, 6);
        directionToIntMap.put(Direction.SOUTH_EAST, 7);
    }
    
    private SyncUtils() {
    }
    
    public static int directionToInt(Direction direction) {
        return directionToIntMap.get(direction);
    }
}
