package eg.game.event.impl;

import eg.game.event.Event;
import eg.game.model.player.Player;

public final class CommandEvent implements Event {
    
    private final Player author;
    private final String command;

    public CommandEvent(Player author, String command) {
        this.author = author;
        this.command = command;
    }
    
    public Player getAuthor() {
        return author;
    }
    
    public String getCommand() {
        return command;
    }
}
