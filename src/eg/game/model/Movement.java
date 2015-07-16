package eg.game.model;

import eg.game.world.Coordinate;
import eg.game.world.Direction;
import eg.game.world.path.Path;
import eg.game.world.path.PathManager;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class Movement {
    
    private final PathManager pathManager;
    
    private Coordinate sectorOrigin;
    private boolean sectorChanging;
    
    private Direction primaryDirection;
    private Direction secondaryDirection;
    
    private Coordinate transitionDestination;
    
    private Coordinate currentCoordinate;
    
    private boolean runningEnabled;
    
    public Movement(Coordinate coordinate) {
        currentCoordinate = coordinate;
        pathManager = new PathManager(coordinate);
        transitionDestination = coordinate;
    }
    
    /**
     * Process done before building the synchronization packet.
     */
    public void preSyncProcess() {
       
        if (transitionDestination != null) {
            pathManager.setPoint(transitionDestination);
            currentCoordinate = transitionDestination;
            primaryDirection = secondaryDirection = Direction.UNKNOWN;
        } else {
            if (pathManager.hasNextPoint()) {
                Coordinate point;
                Coordinate primaryPoint = pathManager.getNextPoint();
                primaryDirection = Direction.get(primaryPoint.getX() - currentCoordinate.getX(),
                        primaryPoint.getY() - currentCoordinate.getY());
                if (runningEnabled && pathManager.hasNextPoint()) {
                    Coordinate secondaryPoint = pathManager.getNextPoint();
                    secondaryDirection = Direction.get(secondaryPoint.getX() - primaryPoint.getX(),
                            secondaryPoint.getY() - primaryPoint.getY());
                    point = secondaryPoint;
                } else {
                    secondaryDirection = Direction.UNKNOWN;
                    point = primaryPoint;
                }
                currentCoordinate = point;
            } else if (primaryDirection != Direction.UNKNOWN) {
                primaryDirection = secondaryDirection = Direction.UNKNOWN; 
            }
        }
        if (sectorOrigin == null || isSectorUpdateRequired()) {
            sectorChanging = true;
            sectorOrigin = new Coordinate(currentCoordinate.getX() - 48 & ~0b111, currentCoordinate.getY() - 48 & ~0b111);
        }
    }
    
    private boolean isSectorUpdateRequired() {
        int dx = currentCoordinate.getX() - sectorOrigin.getX();
        int dy = currentCoordinate.getY() - sectorOrigin.getY();
        return dx < 16 || dx >= 88 || dy < 16 || dy >= 88;
    }
    
    /**
     * Process done after building the synchronization packet.
     */
    public void postSyncProcess() {
        transitionDestination = null;
        sectorChanging = false;
    }
    
    public void setRunningEnabled(boolean enabled) {
        runningEnabled = enabled;
    }
    
    public boolean isRunningEnabled() {
        return runningEnabled;
    }
    
    public Coordinate getCoordinate() {
        return currentCoordinate;
    }
    
    public void setCoordinate(Coordinate coordinate) {
        if (currentCoordinate.equals(coordinate)) {
           pathManager.clearPath();
        } else {
           transitionDestination = coordinate;
        }
    }
    
    public void setPath(Path path) {
        pathManager.setPath(path);
    }
    
    public void clearPath() {
        pathManager.clearPath();
    }
    
    public boolean isSectorChanging() {
        return sectorChanging;
    }
    
    public Coordinate getSectorOrigin() {
        return sectorOrigin;
    }
    
    public Direction getPrimaryDirection() {
        return primaryDirection;
    }
    
    public Direction getSecondaryDirection() {
        return secondaryDirection;
    }
    
    /**
     * Returns {@code true} if the author is standing, {@code false} otherwise.
     */
    public boolean isStanding() {
        return primaryDirection == Direction.UNKNOWN;
    }
    
    /**
     * Returns {@code true} if the author is walking, {@code false} otherwise.
     */
    public boolean isWalking() {
        return primaryDirection != Direction.UNKNOWN && secondaryDirection == Direction.UNKNOWN;
    }
    
    /**
     * Returns {@code true} if the author is running, {@code false} otherwise.
     */
    public boolean isRunning() {
        return secondaryDirection != Direction.UNKNOWN;
    }
    
    /**
     * Returns {@code true} if the author is transiting, {@code false} otherwise.
     */
    public boolean isTransiting() {
        return transitionDestination != null;
    }
    
    /**
     * Returns {@code true} if the author is walking, running or transiting, {@code false} otherwise.
     */
    public boolean isMoving() {
        return transitionDestination != null || primaryDirection != Direction.UNKNOWN;
    }
}
