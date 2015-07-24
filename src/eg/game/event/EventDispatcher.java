package eg.game.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eg.game.model.player.Player;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class EventDispatcher {
    
    private final Map<Class<?>, List<EventListener<?>>> listeners = new HashMap<>();
    
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
        List<EventListener<?>> list = listeners.get(type);
        if (list == null) {
            list = new ArrayList<>();
            listeners.put(type, list);
        } else if (list.contains(listener)) {
            return false;
        }
        return list.add(listener);
    }
    
    public <E extends Event> boolean removeEventListener(Class<E> type, EventListener<E> listener) {
        List<EventListener<?>> list = listeners.get(type);
        if (list == null) {
            return false;
        }
        boolean remove = list.remove(listener);
        if (list.isEmpty()) {
            listeners.remove(type);
        }
        return remove;
    }
}
