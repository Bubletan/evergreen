package eg.game.event.impl;

import eg.game.event.Event;
import eg.game.model.player.Player;

public final class ButtonEvent implements Event {
    
    private final Player author;
    private final int id;

    public ButtonEvent(Player author, int id) {
        this.author = author;
        this.id = id;
    }
    
    public Player getAuthor() {
        return author;
    }
    
    public int getId() {
        return id;
    }
}
