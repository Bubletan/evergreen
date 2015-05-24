package eg.game.model;

import eg.game.world.Coordinate;

public abstract class ImmobileEntity extends Entity {
    
    private Coordinate coordinate;
    
    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
