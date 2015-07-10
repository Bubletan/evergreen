package eg.game.event;

@FunctionalInterface
public interface EventListener<E extends Event<?>> {
    
    public void onEvent(E event);
}
