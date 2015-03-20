package eg.game.model;

public final class Movement {
    
    private final PathManager pathManager;
    
    private Coordinate sectorOrigin;
    private boolean sectorChanging;
    
    private Direction primaryDirection;
    private Direction secondaryDirection;
    
    private Coordinate transitionDestination;
    
    private Coordinate current;
    
    private boolean runningEnabled;
    
    public Movement(Coordinate coordinate) {
        current = coordinate;
        pathManager = new PathManager(new Path.Point(coordinate));
        transitionDestination = coordinate;
    }
    
    /**
     * Process done before building the synchronization packet.
     */
    public void preSyncProcess() {
       
        if (transitionDestination != null) {
            pathManager.setPoint(new Path.Point(transitionDestination));
            current = transitionDestination;
            primaryDirection = secondaryDirection = Direction.NONE;
        } else {
            if (pathManager.hasNextPoint()) {
                int x = current.getX();
                int y = current.getY();
                Path.Point primaryPoint = pathManager.getNextPoint();
                primaryDirection = Direction.forDeltas(primaryPoint.getX() - x, primaryPoint.getY() - y);
                if (runningEnabled && pathManager.hasNextPoint()) {
                    Path.Point secondaryPoint = pathManager.getNextPoint();
                    secondaryDirection = Direction.forDeltas(secondaryPoint.getX() - primaryPoint.getX(),
                            secondaryPoint.getY() - primaryPoint.getY());
                    x = secondaryPoint.getX();
                    y = secondaryPoint.getY();
                } else {
                    secondaryDirection = Direction.NONE;
                    x = primaryPoint.getX();
                    y = primaryPoint.getY();
                }
                current = new Coordinate(x, y, current.getHeight());
            } else {
                primaryDirection = secondaryDirection = Direction.NONE; 
            }
        }
        // TODO if (teleporting) { reset viewing distance; }
        if (sectorOrigin == null || isSectorUpdateRequired()) {
            sectorChanging = true;
            sectorOrigin = new Coordinate(current.getX() - 48 & ~0b111, current.getY() - 48 & ~0b111);
        }
    }
    
    private boolean isSectorUpdateRequired() {
        int dx = current.getX() - sectorOrigin.getX();
        int dy = current.getY() - sectorOrigin.getY();
        return dx < 16 || dx >= 88 || dy < 16 || dy >= 88;
    }
    
    /**
     * Process done after building the synchronization packet.
     */
    public void postSyncProcess() {
        transitionDestination = null;
        sectorChanging = false;
        /*
         * if (!player.isExcessivePlayersSet()) {
         * player.incrementViewingDistance(); } else {
         * player.decrementViewingDistance(); player.resetExcessivePlayers(); }
         */
    }
    
    public Coordinate getCoordinate() {
        return current;
    }
    
    public void setRunningEnabled(boolean enabled) {
        runningEnabled = enabled;
    }
    
    public void setCoordinate(Coordinate coordinate) {
        transitionDestination = coordinate;
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
    
    public boolean isStanding() {
        return primaryDirection == Direction.NONE;
    }
    
    public boolean isWalking() {
        return primaryDirection != Direction.NONE && secondaryDirection == Direction.NONE;
    }
    
    public boolean isRunning() {
        return secondaryDirection != Direction.NONE;
    }
    
    public boolean isTransiting() {
        return transitionDestination != null;
    }
}
