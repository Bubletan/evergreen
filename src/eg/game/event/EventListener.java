package eg.game.event;

@FunctionalInterface
public interface EventListener<T extends Event> {
    
    public void onEvent(T event);
}
