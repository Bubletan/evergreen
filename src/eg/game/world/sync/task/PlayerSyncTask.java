package eg.game.world.sync.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eg.Server;
import eg.game.model.player.Player;
import eg.game.world.sync.SyncBlock;
import eg.game.world.sync.SyncBlockSet;
import eg.game.world.sync.SyncSection;
import eg.game.world.sync.SyncStatus;
import eg.net.game.out.PlayerSyncPacket;
import eg.util.task.Task;

/**
 * @author Bubletan <https://github.com/Bubletan>
 * @author Graham
 */
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
        if (player.getMovement().isTransiting() || player.getMovement().isSectorChanging()) {
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
        int localPlayersCount = player.getSyncContext().getPlayerList().size();
        List<SyncSection> nonLocalSections = new ArrayList<>();
        for (Iterator<Player> it = player.getSyncContext().getPlayerList().iterator(); it.hasNext();) {
            Player p = it.next();
            if (!p.isActive() || p.getMovement().isTransiting()
                    || player.getCoordinate().getBoxDistance(p.getCoordinate()).compareTo(player.getSyncContext().getViewingDistance()) > 0) {
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
            if (p.getSyncContext().getAppearanceCycle() == Server.cycle()) {
                player.getSyncContext().getAppearanceCycleMap().put(p.getIndex(), p.getSyncContext().getAppearanceCycle());
            }
        }
        int added = 0;
        for (Player p : player.getMovement().isMoving() ? Server.world().getPlayerList()
                : Server.world().getMovingPlayerList()) {
            if (player.getSyncContext().getPlayerList().size() >= 255) {
                break;
            }
            if (added >= NEW_PLAYERS_PER_CYCLE) {
                break;
            }
            if (p == player || !player.isActive()
                    || player.getCoordinate().getBoxDistance(p.getCoordinate()).compareTo(player.getSyncContext().getViewingDistance()) > 0
                    || player.getSyncContext().getPlayerList().contains(p)) {
                continue;
            }
            added++;
            player.getSyncContext().getPlayerList().add(p);
            SyncBlockSet blockSet = p.getSyncBlockSet();
            Integer cycle = player.getSyncContext().getAppearanceCycleMap().get(p.getIndex());
            if (!blockSet.contains(SyncBlock.Type.APPEARANCE)
                    && (cycle == null || cycle != p.getSyncContext().getAppearanceCycle())) {
                blockSet = blockSet.clone();
                blockSet.add(new SyncBlock.Appearance(p));
            }
            player.getSyncContext().getAppearanceCycleMap().put(p.getIndex(), p.getSyncContext().getAppearanceCycle());
            nonLocalSections.add(new SyncSection(new SyncStatus.PlayerAddition(
                    p.getIndex(), p.getCoordinate()), blockSet));
        }
        player.getSession().send(new PlayerSyncPacket(localSection, localPlayersCount,
                player.getCoordinate(), player.getMovement().getSectorOrigin(), nonLocalSections));
    }
}
