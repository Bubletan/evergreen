package eg.game.model;

import eg.game.world.Coordinate;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
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
