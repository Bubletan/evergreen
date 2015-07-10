package eg.game.event.impl;

import eg.game.event.Event;
import eg.game.model.player.Player;
import eg.game.world.Coordinate;

public final class MovementEvent implements Event<Player> {
    
    private final Player self;
    private final Coordinate coordinate;
    
    public MovementEvent(Player self, Coordinate coordinate) {
        this.self = self;
        this.coordinate = coordinate;
    }
    
    @Override
    public Player getSelf() {
        return self;
    }
    
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
