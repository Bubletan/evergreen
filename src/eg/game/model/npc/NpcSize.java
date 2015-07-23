package eg.game.model.npc;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public enum NpcSize {
    
    SMALL(1), MEDIUM(2), BIG(3), HUGE(4), VERY_HUGE(5);
    
    private final int value;
    
    private NpcSize(int value) {
        this.value = value;
    }
}
