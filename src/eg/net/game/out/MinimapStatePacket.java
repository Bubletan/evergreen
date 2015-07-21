package eg.net.game.out;

import java.util.Objects;

import eg.net.game.AbstractGamePacket;

public final class MinimapStatePacket implements AbstractGamePacket {
    
    private final State state;
    
    public MinimapStatePacket(State state) {
        this.state = Objects.requireNonNull(state);
    }
    
    public State getState() {
        return state;
    }
    
    public static enum State {
        
        DEFAULT, UNWALKABLE, HIDDEN
    }
}
