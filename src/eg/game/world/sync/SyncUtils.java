package eg.game.world.sync;

import java.util.EnumMap;
import java.util.Map;

import eg.game.model.PrayerHeadicon;
import eg.game.model.SkullHeadicon;
import eg.game.world.Direction;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class SyncUtils {
    
    private static final Map<Direction, Integer> directionToIntMap = new EnumMap<>(Direction.class);
    private static final Map<PrayerHeadicon, Integer> prayerHeadiconToIntMap = new EnumMap<>(PrayerHeadicon.class);
    private static final Map<SkullHeadicon, Integer> skullHeadiconToIntMap = new EnumMap<>(SkullHeadicon.class);
    
    static {
        directionToIntMap.put(Direction.UNKNOWN, -1);
        directionToIntMap.put(Direction.NORTH_WEST, 0);
        directionToIntMap.put(Direction.NORTH, 1);
        directionToIntMap.put(Direction.NORTH_EAST, 2);
        directionToIntMap.put(Direction.WEST, 3);
        directionToIntMap.put(Direction.EAST, 4);
        directionToIntMap.put(Direction.SOUTH_WEST, 5);
        directionToIntMap.put(Direction.SOUTH, 6);
        directionToIntMap.put(Direction.SOUTH_EAST, 7);
        
        prayerHeadiconToIntMap.put(PrayerHeadicon.NONE, -1);
        prayerHeadiconToIntMap.put(PrayerHeadicon.PROTECT_FROM_MELEE, 0);
        prayerHeadiconToIntMap.put(PrayerHeadicon.PROTECT_FROM_MISSILES, 1);
        prayerHeadiconToIntMap.put(PrayerHeadicon.PROTECT_FROM_MAGIC, 2);
        prayerHeadiconToIntMap.put(PrayerHeadicon.RETRIBUTION, 3);
        prayerHeadiconToIntMap.put(PrayerHeadicon.SMITE, 4);
        prayerHeadiconToIntMap.put(PrayerHeadicon.REDEMPTION, 5);
        prayerHeadiconToIntMap.put(PrayerHeadicon.PROTECT_FROM_MAGIC_AND_MISSILES, 6);
        
        skullHeadiconToIntMap.put(SkullHeadicon.NONE, -1);
        skullHeadiconToIntMap.put(SkullHeadicon.WHITE_SKULL, 0);
        skullHeadiconToIntMap.put(SkullHeadicon.RED_SKULL, 1);
    }
    
    private SyncUtils() {
    }
    
    public static int directionToInt(Direction direction) {
        return directionToIntMap.get(direction);
    }
    
    public static int prayerHeadiconToInt(PrayerHeadicon prayerHeadicon) {
        return prayerHeadiconToIntMap.get(prayerHeadicon);
    }
    
    public static int skullHeadiconToInt(SkullHeadicon skullHeadicon) {
        return skullHeadiconToIntMap.get(skullHeadicon);
    }
}
