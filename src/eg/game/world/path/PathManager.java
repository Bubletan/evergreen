package eg.game.world.path;

import eg.game.world.Direction;

public final class PathManager {
    
    private Path.Point point;
    private Path path;
    private int index;
    
    public PathManager(Path.Point origin) {
        point = origin;
    }
    
    public void setPoint(Path.Point point) {
        this.point = point;
        path = null;
    }
    
    public void setPath(Path path) {
        if (path.getLength() == 0) {
            clearPath();
            return;
        }
        int x0 = path.getPoint(0).getX();
        int y0 = path.getPoint(0).getY();
        if (Direction.connectable(x0 - point.getX(), y0 - point.getY())) {
            this.path = new PathBuilder().appendOriginPoint(point).appendPath(path).toPath();
            index = 0;
        } else {
            if (this.path == null) {
                return;
            }
            PathBuilder pb = new PathBuilder().appendOriginPoint(point);
            for (int i = index - 2; i >= 0; i--) {
                if (Direction.connectable(x0 - this.path.getPoint(i).getX(), y0 - this.path.getPoint(i).getY())) {
                    this.path = pb.appendPath(this.path, index - 2, i).appendPath(path).toPath();
                    index = 0;
                    return;
                }
            }
            clearPath();
        }
    }
    
    public void clearPath() {
        index = path.getLength();
    }
    
    public boolean hasNextPoint() {
        return path != null && index != path.getLength();
    }
    
    public Path.Point getNextPoint() {
        if (index == path.getLength()) {
            throw new IllegalStateException("No next point available.");
        }
        return point = path.getPoint(index++);
    }
}
