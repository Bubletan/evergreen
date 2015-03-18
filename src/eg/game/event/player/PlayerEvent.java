package eg.game.event.player;

import eg.game.event.Event;
import eg.game.model.player.Player;

public abstract class PlayerEvent implements Event {
    
    private final Player author;
    
    public PlayerEvent(Player author) {
        this.author = author;
    }
    
    public Player getAuthor() {
        return author;
    }
}
