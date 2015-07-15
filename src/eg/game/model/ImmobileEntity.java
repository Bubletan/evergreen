package eg.game.model;

import eg.game.world.Coordinate;

public abstract class ImmobileEntity extends Entity {
    
    private final Coordinate coordinate;
    
    public ImmobileEntity(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
    
    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
