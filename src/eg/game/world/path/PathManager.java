package eg.game.world.path;

import eg.game.world.Direction;
import eg.game.world.path.Path.Point;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class PathManager {
    
    private Point point;
    private Path path;
    private int index;
    
    public PathManager(Point point) {
        this.point = point;
    }
    
    public void setPoint(Point point) {
        this.point = point;
        path = null;
    }
    
    public void setPath(Path path) {
        if (path.getLength() == 0) {
            clearPath();
            return;
        }
        if (path.getPoint(0).equals(point)) {
            this.path = path;
            index = 1;
            return;
        }
        int x0 = path.getPoint(0).getX();
        int y0 = path.getPoint(0).getY();
        if (Direction.connectable(x0 - point.getX(), y0 - point.getY())) {
            this.path = new PathBuilder().appendJumpPoint(point).appendPath(path).toPath();
            index = 1;
            return;
        }
        if (this.path == null) {
            return;
        }
        PathBuilder pb = new PathBuilder().appendJumpPoint(point);
        for (int i = index - 2; i >= 0; i--) {
            if (Direction.connectable(x0 - this.path.getPoint(i).getX(), y0 - this.path.getPoint(i).getY())) {
                this.path = pb.appendPath(this.path, index - 2, i).appendPath(path).toPath();
                index = 1;
                return;
            }
        }
        clearPath();
    }
    
    public void clearPath() {
        if (path == null || index == path.getLength()) {
            return;
        }
        if (index <= 1) {
            path = null;
            return;
        }
        path = new PathBuilder().appendPath(path, 0, index - 1).toPath();
    }
    
    public boolean hasNextPoint() {
        return path != null && index != path.getLength();
    }
    
    public Point getNextPoint() {
        if (index == path.getLength()) {
            throw new IllegalStateException("No next point available.");
        }
        return point = path.getPoint(index++);
    }
}
