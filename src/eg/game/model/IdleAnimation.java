package eg.game.model;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class IdleAnimation {
    
    public static final IdleAnimation DEFAULT = new IdleAnimation(808, 823, 819, 820, 821, 822, 824);
    
    private final int stand;
    private final int turn;
    private final int walk;
    private final int turn180;
    private final int turn90Cw;
    private final int turn90Ccw;
    private final int run;
    
    public IdleAnimation(int stand, int turn, int walk, int turn180, int turn90Cw, int turn90Ccw, int run) {
        this.stand = stand;
        this.turn = turn;
        this.walk = walk;
        this.turn180 = turn180;
        this.turn90Cw = turn90Cw;
        this.turn90Ccw = turn90Ccw;
        this.run = run;
    }
    
    public int getStand() {
        return stand;
    }
    
    public int getTurn() {
        return turn;
    }
    
    public int getWalk() {
        return walk;
    }
    
    public int getTurn180() {
        return turn180;
    }
    
    public int getTurn90Cw() {
        return turn90Cw;
    }
    
    public int getTurn90Ccw() {
        return turn90Ccw;
    }
    
    public int getRun() {
        return run;
    }
}
