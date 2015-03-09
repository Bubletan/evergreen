package eg.game.model;

public final class IdleAnimation {
    
    private static final int DEFAULT_STAND = 808;
    private static final int DEFAULT_TURN = 823;
    private static final int DEFAULT_WALK = 819;
    private static final int DEFAULT_TURN_180 = 820;
    private static final int DEFAULT_TURN_90_CW = 821;
    private static final int DEFAULT_TURN_90_CCW = 822;
    private static final int DEFAULT_RUN = 824;
    
    private int stand = DEFAULT_STAND;
    private int turn = DEFAULT_TURN;
    private int walk = DEFAULT_WALK;
    private int turn180 = DEFAULT_TURN_180;
    private int turn90Cw = DEFAULT_TURN_90_CW;
    private int turn90Ccw = DEFAULT_TURN_90_CCW;
    private int run = DEFAULT_RUN;
    
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
