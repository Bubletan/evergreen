package eg.game.event.impl;

import eg.game.event.Event;
import eg.game.world.Coordinate;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class MovementEvent implements Event {
    
    private final Coordinate coordinate;
    
    public MovementEvent(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
    
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
