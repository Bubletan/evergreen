package eg.game.event;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
@FunctionalInterface
public interface EventListener<E extends Event<?>> {
    
    public void onEvent(E event);
}
