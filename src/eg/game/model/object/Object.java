package eg.game.model.object;

import eg.game.model.ImmobileEntity;
import eg.game.world.Coordinate;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class Object extends ImmobileEntity {
    
    private final ObjectType type;
    private final ObjectKind kind;
    private final ObjectOrientation orientation;
    
    public Object(ObjectType type, ObjectKind kind, ObjectOrientation orientation, Coordinate coordinate) {
        super(coordinate);
        this.type = type;
        this.kind = kind;
        this.orientation = orientation;
    }
    
    public ObjectType getType() {
        return type;
    }
    
    public ObjectKind getKind() {
        return kind;
    }
    
    public ObjectOrientation getOrientation() {
        return orientation;
    }
}
