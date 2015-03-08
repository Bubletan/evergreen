package eg.game.model;

public final class Movement {
	
	private MovementProvider provider;
	
	private Coordinate sectorOrigin;
	private boolean sectorChanging;
	
	private Coordinate teleportDestination;
	
	private Coordinate current;
	
	public Movement(Coordinate coordinate) {
		current = coordinate;
		this.provider = new MovementProvider(coordinate.getX(), coordinate.getY());
		teleportDestination = coordinate;
	}
	
	/** 
	 * Process done before building the synchronization packet.
	 */
	public void preSyncProcess() {
		int x, y, height;
		if (teleportDestination != null) {
			provider.clearPath();
			x = teleportDestination.getX();
			y = teleportDestination.getY();
			provider.setPosition(x, y);
			height = teleportDestination.getHeight();
		} else {
			provider.nextMoment();
			x = provider.getCurrentX();
			y = provider.getCurrentY();
			height = current.getHeight();
		}
		if (!current.equals(x, y, height)) {
			current = new Coordinate(x, y, height);
		}
		// TODO if (teleporting) { reset viewing distance; }
		if (sectorOrigin == null || isSectorUpdateRequired()) {
			sectorChanging = true;
			sectorOrigin = new Coordinate(current.getX() - 48 & ~0b111,
					current.getY() - 48 & ~0b111);
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
		teleportDestination = null;
		sectorChanging = false;
		/*
		if (!player.isExcessivePlayersSet()) {
			player.incrementViewingDistance();
		} else {
			player.decrementViewingDistance();
			player.resetExcessivePlayers();
		}
		*/
	}
	
	public Coordinate getCoordinate() {
		return current;
	}
	
	public MovementProvider getProvider() {
		return provider;
	}
	
	public boolean isSectorChanging() {
		return sectorChanging;
	}
	
	public Coordinate getSectorOrigin() {
		return sectorOrigin;
	}
	
	public Direction getPrimaryDirection() {
		return provider.getPrimaryDir();
	}
	
	public Direction getSecondaryDirection() {
		return provider.getSecondaryDir();
	}
	
	public boolean isStanding() {
		return provider.getPrimaryDir() == Direction.NONE;
	}
	
	public boolean isWalking() {
		return provider.getPrimaryDir() != Direction.NONE &&
				provider.getSecondaryDir() == Direction.NONE;
	}
	
	public boolean isRunning() {
		return provider.getSecondaryDir() != Direction.NONE;
	}
	
	public boolean isTeleporting() {
		return teleportDestination != null;
	}
	
	public void teleportTo(Coordinate coordinate) {
		teleportDestination = coordinate;
	}
	
	public void stop() {
		provider.clearPath();
	}
}
