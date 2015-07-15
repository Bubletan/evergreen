package eg.game.event.impl;

import eg.game.event.Event;
import eg.game.model.player.Player;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class CommandEvent implements Event<Player> {
    
    private final Player self;
    private final String command;

    public CommandEvent(Player self, String command) {
        this.self = self;
        this.command = command;
    }
    
    @Override
    public Player getSelf() {
        return self;
    }
    
    public String getCommand() {
        return command;
    }
}
