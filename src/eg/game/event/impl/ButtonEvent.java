package eg.game.event.impl;

import eg.game.event.Event;
import eg.game.model.player.Player;

public final class ButtonEvent implements Event<Player> {
    
    private final Player self;
    private final int id;

    public ButtonEvent(Player self, int id) {
        this.self = self;
        this.id = id;
    }
    
    @Override
    public Player getSelf() {
        return self;
    }
    
    public int getId() {
        return id;
    }
}
