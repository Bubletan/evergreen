package eg.game.event.impl;

import eg.game.event.Event;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class CommandEvent implements Event {
    
    private final String command;

    public CommandEvent(String command) {
        this.command = command;
    }
    
    public String getCommand() {
        return command;
    }
}
