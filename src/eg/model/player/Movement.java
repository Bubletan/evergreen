package eg.model.player;

import eg.model.Coordinate;
import eg.model.Direction;
import eg.model.MovementProvider;

public final class Movement {
	
	private Player player;
	private MovementProvider provider;
	
	private Coordinate sectorOrigin;
	private boolean sectorChanging;
	
	private Coordinate teleportDestination;
	
	public Movement(Player player) {
		this.player = player;
		Coordinate coord = player.getCoordinate();
		this.provider = new MovementProvider(coord.getX(), coord.getY());
		teleportDestination = coord;
	}
	
	/**
	 * Process done before building the synchronization packet.
	 */
	public void preSyncProcess() {
		provider.nextMoment();
		int x = provider.getCurrentX();
		int y = provider.getCurrentY();
		Coordinate coord = player.getCoordinate();
		if (coord.getX() != x || coord.getY() != y) {
			player.setCoordinate(new Coordinate(x, y, coord.getHeight()));
		}
		// TODO if (teleporting) { reset viewing distance; }
		if (sectorOrigin == null || isSectorUpdateRequired()) {
			sectorChanging = true;
			sectorOrigin = new Coordinate(player.getCoordinate().getX() - 48 & ~0b111,
					player.getCoordinate().getY() - 48 & ~0b111);
		}
	}
	
	private boolean isSectorUpdateRequired() {
		Coordinate current = player.getCoordinate();
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
	
	public MovementProvider getProvider() {
		return provider;
	}
	
	public boolean isSectorChanging() {
		return sectorChanging;
	}
	
	public Coordinate getSectorOrigin() {
		return sectorOrigin;
	}
	
	public Direction getPrimaryDir() {
		return provider.getPrimaryDir();
	}
	
	public Direction getSecondaryDir() {
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
}
