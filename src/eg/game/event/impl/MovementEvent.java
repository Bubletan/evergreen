package eg.game.event.impl;

import eg.game.event.Event;
import eg.game.model.player.Player;
import eg.game.world.Coordinate;

public final class MovementEvent implements Event {
    
    private final Player author;
    private final Coordinate coordinate;
    
    public MovementEvent(Player author, Coordinate coordinate) {
        this.author = author;
        this.coordinate = coordinate;
    }
    
    public Player getAuthor() {
        return author;
    }
    
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
