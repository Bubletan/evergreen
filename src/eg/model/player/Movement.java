package eg.model.player;

import eg.model.Coordinate;
import eg.model.Direction;
import eg.model.MovementProvider;

public final class Movement {
	
	private Player player;
	private MovementProvider provider;
	
	private Coordinate lastKnownSector;
	private boolean sectorChanging;
	
	private Coordinate teleportDestination;
	
	public Movement(Player player) {
		this.player = player;
		Coordinate coord = player.getCoord();
		this.provider = new MovementProvider(coord.getX(), coord.getY());
		teleportDestination = coord;
	}
	
	/**
	 * Process done before building the synchronization packet.
	 */
	public void preSyncProcess() {
		provider.nextMoment();
		player.setCoord(player.getCoord().at(provider.getCurrentX(), provider.getCurrentY()));
		// TODO if (teleporting) { reset viewing distance; }
		if (lastKnownSector == null || isSectorUpdateRequired()) {
			sectorChanging = true;
			lastKnownSector = player.getCoord();
		}
	}
	
	private boolean isSectorUpdateRequired() {
		Coordinate current = player.getCoord();
		int dx = current.getRelativeX(lastKnownSector);
		int dy = current.getRelativeY(lastKnownSector);
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
	
	public Coordinate getLastKnownSector() {
		return lastKnownSector;
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
