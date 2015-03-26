package eg.game.world.sync;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eg.game.model.npc.Npc;
import eg.game.model.player.Player;

public final class SyncContext {
    
    private final List<Player> playerList = new LinkedList<>();
    private final List<Npc> npcList = new LinkedList<>();
    private final Map<Integer, Integer> appearanceCycleMap = new HashMap<>();
    private int appearanceCycle;
    
    public SyncContext() {
    }
    
    public List<Player> getPlayerList() {
        return playerList;
    }
    
    public List<Npc> getNpcList() {
        return npcList;
    }
    
    public Map<Integer, Integer> getAppearanceCycleMap() {
        return appearanceCycleMap;
    }
    
    public int getAppearanceCycle() {
        return appearanceCycle;
    }
    
    public void setAppearanceCycle(int value) {
        appearanceCycle = value;
    }
    
    public int getViewingDistance() {
        return 15;
    }
}
