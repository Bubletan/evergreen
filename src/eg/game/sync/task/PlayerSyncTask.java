package eg.game.sync.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eg.Server;
import eg.game.model.player.Player;
import eg.game.sync.SyncBlock;
import eg.game.sync.SyncBlockSet;
import eg.game.sync.SyncSection;
import eg.game.sync.SyncStatus;
import eg.net.game.out.PlayerSyncPacket;
import eg.util.task.Task;

public final class PlayerSyncTask implements Task {
    
    private static final int NEW_PLAYERS_PER_CYCLE = 20;
    
    private static final SyncStatus SYNC_STATUS_STAND = new SyncStatus.Stand();
    private static final SyncStatus SYNC_STATUS_REMOVAL = new SyncStatus.Removal();
    
    private static final SyncBlockSet emptyBlockSet = new SyncBlockSet();
    
    private final Player player;
    
    public PlayerSyncTask(Player player) {
        this.player = player;
    }
    
    @Override
    public void execute() {
        SyncSection localSection;
        SyncBlockSet localBlockSet = player.getSyncBlockSet();
        if (localBlockSet.contains(SyncBlock.Type.CHAT_MESSAGE)) {
            localBlockSet = localBlockSet.clone();
            localBlockSet.remove(SyncBlock.Type.CHAT_MESSAGE);
        }
        if (player.getMovement().isTeleporting() || player.getMovement().isSectorChanging()) {
            localSection = new SyncSection(new SyncStatus.Transition(player.getCoordinate(),
                    player.getMovement().isSectorChanging()), localBlockSet);
        } else if (player.getMovement().isRunning()) {
            localSection = new SyncSection(new SyncStatus.Run(player.getMovement().getPrimaryDirection(),
                    player.getMovement().getSecondaryDirection()), localBlockSet);
        } else if (player.getMovement().isWalking()) {
            localSection = new SyncSection(new SyncStatus.Walk(player.getMovement().getPrimaryDirection()), localBlockSet);
        } else {
            localSection = new SyncSection(SYNC_STATUS_STAND, localBlockSet);
        }
        int localPlayersCount = player.getLocalPlayerList().size();
        List<SyncSection> nonLocalSections = new ArrayList<>();
        for (Iterator<Player> it = player.getLocalPlayerList().iterator(); it.hasNext();) {
            Player p = it.next();
            if (!p.isActive() || p.getMovement().isTeleporting()
                    || player.getCoordinate().getBoxDistance(p.getCoordinate()) > player.getViewingDistance()) {
                it.remove();
                nonLocalSections.add(new SyncSection(SYNC_STATUS_REMOVAL, emptyBlockSet));
                continue;
            }
            if (p.getMovement().isRunning()) {
                nonLocalSections.add(new SyncSection(new SyncStatus.Run(p.getMovement().getPrimaryDirection(),
                        p.getMovement().getSecondaryDirection()), p.getSyncBlockSet()));
            } else if (p.getMovement().isWalking()) {
                nonLocalSections.add(new SyncSection(new SyncStatus.Walk(p.getMovement().getPrimaryDirection()),
                        p.getSyncBlockSet()));
            } else {
                nonLocalSections.add(new SyncSection(SYNC_STATUS_STAND, p.getSyncBlockSet()));
            }
            if (p.getAppearanceCycle() == Server.cycle()) {
                player.getLocalAppearanceCycleMap().put(p.getIndex(), p.getAppearanceCycle());
            }
        }
        int added = 0;
        for (Player p : Server.world().getPlayerList()) { // TODO players on region
            if (player.getLocalPlayerList().size() >= 255) {
                break;
            }
            if (added >= NEW_PLAYERS_PER_CYCLE) {
                break;
            }
            if (p == player || !player.isActive()
                    || player.getCoordinate().getBoxDistance(p.getCoordinate()) > player.getViewingDistance()
                    || player.getLocalPlayerList().contains(p)) {
                continue;
            }
            System.out.println(player.getUsername() + ": " + "added " + p.getUsername());
            added++;
            player.getLocalPlayerList().add(p);
            SyncBlockSet blockSet = p.getSyncBlockSet();
            Integer cycle = player.getLocalAppearanceCycleMap().get(p.getIndex());
            if (!blockSet.contains(SyncBlock.Type.APPEARANCE)
                    && (cycle == null || cycle != p.getAppearanceCycle())) {
                blockSet = blockSet.clone();
                blockSet.add(new SyncBlock.Appearance(p));
            }
            player.getLocalAppearanceCycleMap().put(p.getIndex(), p.getAppearanceCycle());
            nonLocalSections.add(new SyncSection(new SyncStatus.PlayerAddition(
                    p.getIndex(), p.getCoordinate()), blockSet));
        }
        player.getSession().send(new PlayerSyncPacket(localSection, localPlayersCount,
                player.getCoordinate(), player.getMovement().getSectorOrigin(), nonLocalSections));
    }
}
