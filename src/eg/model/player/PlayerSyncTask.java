package eg.model.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eg.global.World;
import eg.model.sync.SyncBlock;
import eg.model.sync.SyncBlockSet;
import eg.model.sync.SyncSection;
import eg.model.sync.SyncStatus;
import eg.net.game.out.MapLoadingPacket;
import eg.net.game.out.PlayerSyncPacket;
import eg.util.task.Task;

public final class PlayerSyncTask implements Task {
	
	private static final int NEW_PLAYERS_PER_CYCLE = 20;
	
	private static final SyncStatus.Stand STAND_STATUS = new SyncStatus.Stand();
	private static final SyncStatus.Removal REMOVAL_STATUS = new SyncStatus.Removal();
	
	private static final SyncBlockSet emptyBlockSet = new SyncBlockSet();
	
	@Override
	public void execute() {
		
		World.getWorld().getPlayerList().parallelStream()
				.forEach(this::preSyncProcess);
		
		World.getWorld().getPlayerList().parallelStream()
				.forEach(this::syncProcess);
		
		World.getWorld().getPlayerList().parallelStream()
				.forEach(this::postSyncProcess);
	}
	
	private void preSyncProcess(Player player) {
		
		player.getMovement().preSyncProcess();
		
		if (player.getMovement().isSectorChanging()) {
			player.getSession().send(new MapLoadingPacket(player.getCoordinate()));
		}
	}
	
	private void postSyncProcess(Player player) {
		
		player.getMovement().postSyncProcess();
		player.resetSyncBlockSet();
	}
	
	private void syncProcess(Player player) {
		
		SyncSection localSection;
		SyncBlockSet localBlockSet = player.getSyncBlockSet();
		if (localBlockSet.contains(SyncBlock.Type.CHAT_MESSAGE)) {
			localBlockSet = localBlockSet.clone();
			localBlockSet.remove(SyncBlock.Type.CHAT_MESSAGE);
		}
		if (player.getMovement().isTeleporting() || player.getMovement().isSectorChanging()) {
			localSection = new SyncSection(new SyncStatus.Transition(player.getCoordinate(),
					player.getMovement().getSectorOrigin(), player.getMovement().isTeleporting()), 
					localBlockSet);
		} else if (player.getMovement().isRunning()) {
			localSection = new SyncSection(new SyncStatus.Run(player.getMovement().getPrimaryDir(),
					player.getMovement().getSecondaryDir()), localBlockSet);
		} else if (player.getMovement().isWalking()) {
			localSection = new SyncSection(new SyncStatus.Walk(player.getMovement().getPrimaryDir()),
					localBlockSet);
		} else {
			localSection = new SyncSection(STAND_STATUS, localBlockSet);
		}
		int localPlayersCount = player.getLocalPlayers().size();
		List<SyncSection> nonLocalSections = new ArrayList<>();
		for (Iterator<Player> it = player.getLocalPlayers().iterator(); it.hasNext();) {
			Player p = it.next();
			if (!p.isActive() || p.getMovement().isTeleporting() ||
					player.getCoordinate().getBoxDistance(p.getCoordinate()) >
					player.getViewingDistance()) {
				it.remove();
				nonLocalSections.add(new SyncSection(REMOVAL_STATUS, emptyBlockSet));
			} else if (p.getMovement().isRunning()) {
				nonLocalSections.add(new SyncSection(new SyncStatus.Run(p.getMovement().getPrimaryDir(),
						p.getMovement().getSecondaryDir()),
						p.getSyncBlockSet()));
			} else if (p.getMovement().isWalking()) {
				nonLocalSections.add(new SyncSection(new SyncStatus.Walk(p.getMovement().getPrimaryDir()),
						p.getSyncBlockSet()));
			} else {
				nonLocalSections.add(new SyncSection(STAND_STATUS, p.getSyncBlockSet()));
			}
		}
		int added = 0;
		for (Player p : World.getWorld().getPlayerList()) { //TODO players on region
			if (player.getLocalPlayers().size() >= 255) {
				break;
			}
			if (added >= NEW_PLAYERS_PER_CYCLE) {
				break;
			}
			if (p == player || !player.isActive() ||
					player.getCoordinate().getBoxDistance(p.getCoordinate()) >
					player.getViewingDistance() ||
					player.getLocalPlayers().contains(p)) {
				continue;
			}
			added++;
			player.getLocalPlayers().add(p);
			SyncBlockSet blockSet = p.getSyncBlockSet();
			if (!blockSet.contains(SyncBlock.Type.APPEARANCE)) {
				blockSet = blockSet.clone();
				blockSet.add(new SyncBlock.Appearance(p));
			}
			nonLocalSections.add(new SyncSection(new SyncStatus.Addition(p.getIndex(), p.getCoordinate()),
					blockSet));
		}
		player.getSession().send(new PlayerSyncPacket(localSection, localPlayersCount,
				player.getCoordinate(), nonLocalSections));
	}
}
