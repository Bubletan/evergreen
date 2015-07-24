package eg.game.event;

import eg.game.model.player.Player;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
@FunctionalInterface
public interface EventListener<E extends Event> {
    
    public void onEvent(Player self, E event);
}
