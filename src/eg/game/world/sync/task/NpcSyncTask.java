package eg.game.world.sync.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eg.Server;
import eg.game.model.npc.Npc;
import eg.game.model.player.Player;
import eg.game.world.sync.SyncBlockSet;
import eg.game.world.sync.SyncSection;
import eg.game.world.sync.SyncStatus;
import eg.net.game.out.NpcSyncPacket;
import eg.util.task.Task;

/**
 * @author Bubletan <https://github.com/Bubletan>
 * @author Major
 */
public final class NpcSyncTask implements Task {
    
    private static final int NEW_NPCS_PER_CYCLE = 20;
    
    private static final SyncStatus SYNC_STATUS_REMOVAL = new SyncStatus.Removal();
    private static final SyncStatus SYNC_STATUS_STAND = new SyncStatus.Stand();
    
    private static final SyncBlockSet emptyBlockSet = new SyncBlockSet();
    
    private final Player player;
    
    public NpcSyncTask(Player player) {
        this.player = player;
    }
    
    @Override
    public void execute() {
        int localNpcCount = player.getSyncContext().getNpcList().size();
        List<SyncSection> sections = new ArrayList<>();
        for (Iterator<Npc> it = player.getSyncContext().getNpcList().iterator(); it.hasNext();) {
            Npc npc = it.next();
            if (!npc.isActive() || npc.getMovement().isTransiting()
                    || player.getCoordinate().getBoxDistance(npc.getCoordinate()) > player.getSyncContext().getViewingDistance()) {
                it.remove();
                sections.add(new SyncSection(SYNC_STATUS_REMOVAL, emptyBlockSet));
            } else if (npc.getMovement().isRunning()) {
                sections.add(new SyncSection(new SyncStatus.Run(npc.getMovement().getPrimaryDirection(),
                        npc.getMovement().getSecondaryDirection()), npc.getSyncBlockSet()));
            } else if (npc.getMovement().isWalking()) {
                sections.add(new SyncSection(new SyncStatus.Walk(npc.getMovement().getPrimaryDirection()),
                        npc.getSyncBlockSet()));
            } else {
                sections.add(new SyncSection(SYNC_STATUS_STAND, npc.getSyncBlockSet()));
            }
        }
        int added = 0;
        for (Npc npc : player.getMovement().isMoving() ? Server.world().getNpcList()
                : Server.world().getMovingNpcList()) {
            if (player.getSyncContext().getNpcList().size() >= 255) {
                break;
            }
            if (added >= NEW_NPCS_PER_CYCLE) {
                break;
            }
            if (!npc.isActive() || player.getCoordinate().getBoxDistance(npc.getCoordinate()) > player.getSyncContext().getViewingDistance()
                    || player.getSyncContext().getNpcList().contains(npc)) {
                continue;
            }
            added++;
            player.getSyncContext().getNpcList().add(npc);
            sections.add(new SyncSection(new SyncStatus.NpcAddition(npc.getIndex(), npc.getCoordinate(), npc.getType()),
                    npc.getSyncBlockSet()));
        }
        player.getSession().send(new NpcSyncPacket(player.getCoordinate(), sections, localNpcCount));
    }
}
