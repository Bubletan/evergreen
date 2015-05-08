package eg.game.world;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eg.Config;
import eg.game.event.EventDispatcher;
import eg.game.model.npc.Npc;
import eg.game.model.player.Player;

public final class World {
    
    private final EventDispatcher eventDispatcher = new EventDispatcher();
    
    private int playerCount;
    private final Map<Long, Player> playerByHash = new HashMap<>(Config.MAX_PLAYERS);
    private final Player[] playerByIndex = new Player[Config.MAX_PLAYERS];
    private List<Player> playerList;
    private List<Player> movingPlayerList;
    
    private int npcCount;
    private final Npc[] npcByIndex = new Npc[Config.MAX_NPCS];
    private List<Npc> npcList;
    private List<Npc> movingNpcList;
    
    public World() {
    }
    
    public void syncLists() {
        
        List<Player> players = Arrays.stream(playerByIndex).parallel()
                .filter(Objects::nonNull).collect(Collectors.toList());
        List<Npc> npcs = Arrays.stream(npcByIndex).parallel()
                .filter(Objects::nonNull).collect(Collectors.toList());
        
        playerList = Collections.unmodifiableList(players);
        npcList = Collections.unmodifiableList(npcs);
        
        movingPlayerList = playerList.stream().filter(p -> p.getMovement().isMoving()).collect(Collectors.toList());
        movingNpcList = npcList.stream().filter(n -> n.getMovement().isMoving()).collect(Collectors.toList());
    }
    
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }
    
    public boolean addPlayer(Player player) {
        if (playerCount >= Config.MAX_PLAYERS) {
            return false;
        }
        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
            if (playerByIndex[i] == null) {
                playerByIndex[i] = player;
                playerCount++;
                playerByHash.put(player.getHash(), player);
                player.setIndex(i + 1);
                return true;
            }
        }
        return false;
    }
    
    public boolean removePlayer(Player player) {
        int index = player.getIndex() - 1;
        if (playerByIndex[index] != player) {
            return false;
        }
        playerByIndex[index] = null;
        playerCount--;
        playerByHash.remove(player.getHash());
        return true;
    }
    
    public Player getPlayerForHash(long hash) {
        return playerByHash.get(hash);
    }
    
    public List<Player> getPlayerList() {
        return playerList;
    }
    
    public Stream<Player> getPlayerStream() {
        return playerList.parallelStream();
    }
    
    public int getPlayerCount() {
        return playerList.size();
    }
    
    public List<Player> getMovingPlayerList() {
        return movingPlayerList;
    }
    
    public boolean addNpc(Npc npc) {
        if (npcCount >= Config.MAX_NPCS) {
            return false;
        }
        for (int i = 0; i < Config.MAX_NPCS; i++) {
            if (npcByIndex[i] == null) {
                npcByIndex[i] = npc;
                npcCount++;
                npc.setIndex(i + 1);
                return true;
            }
        }
        return false;
    }
    
    public boolean removeNpc(Npc npc) {
        int index = npc.getIndex() - 1;
        if (npcByIndex[index] != npc) {
            return false;
        }
        npcByIndex[index] = null;
        npcCount--;
        return true;
    }
    
    public List<Npc> getNpcList() {
        return npcList;
    }
    
    public Stream<Npc> getNpcStream() {
        return npcList.parallelStream();
    }
    
    public int getNpcCount() {
        return npcList.size();
    }
    
    public List<Npc> getMovingNpcList() {
        return movingNpcList;
    }
}
