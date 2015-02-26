package eg.model.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eg.global.World;
import eg.model.sync.SyncBlockSet;
import eg.model.sync.SyncSegment;
import eg.model.sync.block.AppearanceBlock;
import eg.model.sync.block.ChatBlock;
import eg.model.sync.seg.AddSegment;
import eg.model.sync.seg.RemoveSegment;
import eg.model.sync.seg.RunSegment;
import eg.model.sync.seg.StillSegment;
import eg.model.sync.seg.TransitionSegment;
import eg.model.sync.seg.WalkSegment;
import eg.net.game.out.MapLoadingPacket;
import eg.net.game.out.PlayerSyncPacket;
import eg.util.task.Task;

public final class PlayerSyncTask implements Task {
	
	private static final int NEW_PLAYERS_PER_CYCLE = 20;
	
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
			player.getSession().send(new MapLoadingPacket(player.getCoord()));
		}
	}
	
	private void postSyncProcess(Player player) {
		
		player.getMovement().postSyncProcess();
	}
	
	private void syncProcess(Player player) {
		
		SyncSegment localSegment;
		SyncBlockSet localBlockSet = player.getSyncBlockSet();
		if (localBlockSet.contains(ChatBlock.class)) {
			localBlockSet = localBlockSet.clone();
			localBlockSet.remove(ChatBlock.class);
		}
		if (player.getMovement().isTeleporting() || player.getMovement().isSectorChanging()) {
			localSegment = new TransitionSegment(localBlockSet, player.getCoord(),
					player.getMovement().isTeleporting(), player.getMovement().getLastKnownSector());
		} else if (player.getMovement().isRunning()) {
			localSegment = new RunSegment(localBlockSet, player.getMovement().getPrimaryDir(),
					player.getMovement().getSecondaryDir());
		} else if (player.getMovement().isWalking()) {
			localSegment = new WalkSegment(localBlockSet, player.getMovement().getPrimaryDir());
		} else {
			localSegment = new StillSegment(localBlockSet);
		}
		int localPlayersCount = player.getLocalPlayers().size();
		List<SyncSegment> nonLocalSegments = new ArrayList<>();
		for (Iterator<Player> it = player.getLocalPlayers().iterator(); it.hasNext();) {
			Player p = it.next();
			if (!p.isActive() || p.getMovement().isTeleporting() ||
					player.getCoord().getBoxDistance(p.getCoord()) >
					player.getViewingDistance()) {
				it.remove();
				nonLocalSegments.add(new RemoveSegment());
			} else if (p.getMovement().isRunning()) {
				nonLocalSegments.add(new RunSegment(p.getSyncBlockSet(), p.getMovement().getPrimaryDir(),
						p.getMovement().getSecondaryDir()));
			} else if (p.getMovement().isWalking()) {
				nonLocalSegments.add(new WalkSegment(p.getSyncBlockSet(), p.getMovement().getPrimaryDir()));
			} else {
				nonLocalSegments.add(new StillSegment(p.getSyncBlockSet()));
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
					player.getCoord().getBoxDistance(p.getCoord()) >
					player.getViewingDistance() ||
					player.getLocalPlayers().contains(p)) {
				continue;
			}
			added++;
			player.getLocalPlayers().add(p);
			SyncBlockSet blockSet = p.getSyncBlockSet();
			if (!blockSet.contains(AppearanceBlock.class)) {
				blockSet = blockSet.clone();
				blockSet.add(new AppearanceBlock(p));
			}
			nonLocalSegments.add(new AddSegment(blockSet, p.getIndex(),
					p.getCoord(), player.getCoord()));
		}
		player.getSession().send(new PlayerSyncPacket(localSegment, localPlayersCount, nonLocalSegments));
	}
}
