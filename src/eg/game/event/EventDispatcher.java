package eg.game.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EventDispatcher<T extends Event> {
    
    private final Map<Class<? extends T>, List<EventListener<? extends T>>> listeners = new HashMap<>();
    private final Class<T> supertype;
    
    public EventDispatcher(Class<T> type) {
        supertype = type;
    }
    
    public <E extends T> boolean dispatchEvent(E event) {
        boolean fired = false;
        Class<?> type = event.getClass();
        do {
            List<EventListener<? extends T>> list = listeners.get(type);
            if (list != null) {
                fired = true;
                for (EventListener<? extends T> listener : list) {
                    @SuppressWarnings("unchecked")
                    EventListener<? super E> castListener = (EventListener<? super E>) listener;
                    castListener.onEvent(event);
                }
            }
            type = type.getSuperclass();
        } while (supertype.isAssignableFrom(type));
        return fired;
    }
    
    public <E extends T> boolean addEventListener(Class<E> type, EventListener<E> listener) {
        List<EventListener<? extends T>> list = listeners.get(type);
        if (list == null) {
            list = new ArrayList<>();
            listeners.put(type, list);
        } else if (list.contains(listener)) {
            return false;
        }
        return list.add(listener);
    }
    
    public <E extends T> boolean removeEventListener(Class<E> type, EventListener<E> listener) {
        List<EventListener<? extends T>> list = listeners.get(type);
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
