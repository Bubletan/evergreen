package eg.game.event.impl;

import eg.game.event.Event;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class ButtonEvent implements Event {
    
    private final int id;

    public ButtonEvent(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}
