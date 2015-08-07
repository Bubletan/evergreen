package eg.game.event;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import eg.game.model.player.Player;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class EventDispatcher {
    
    private final ListMultimap<Class<?>, EventListener<?>> listeners = ArrayListMultimap.create();
    
    public EventDispatcher() {
    }
    
    public <E extends Event> boolean dispatchEvent(Player self, E event) {
        boolean fired = false;
        Class<?> type = event.getClass();
        do {
            List<EventListener<?>> list = listeners.get(type);
            if (list != null) {
                fired = true;
                for (EventListener<?> listener : list) {
                    @SuppressWarnings("unchecked")
                    EventListener<? super E> castListener = (EventListener<? super E>) listener;
                    try {
                        castListener.onEvent(self, event);
                    } catch (Exception e) {
                        System.err.println("Error in event listener.");
                        e.printStackTrace();
                    }
                }
            }
            type = type.getSuperclass();
        } while (Event.class.isAssignableFrom(type));
        return fired;
    }
    
    public <E extends Event> boolean addEventListener(Class<E> type, EventListener<E> listener) {
        return listeners.put(type, listener);
    }
    
    public <E extends Event> boolean removeEventListener(Class<E> type, EventListener<E> listener) {
        return listeners.remove(type, listener);
    }
}
