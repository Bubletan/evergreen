package eg.global;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import eg.model.npc.Npc;
import eg.model.player.Player;

public final class WorldSector {
	
	public WorldSector() {
	}
	
	private final List<Player> playerList = new LinkedList<>();
	private final List<Npc> npcList = new LinkedList<>();
	
	public void addPlayer(Player player) {
		playerList.add(player);
	}
	
	public void removePlayer(Player player) {
		playerList.remove(player);
	}
	
	public void addNpc(Npc npc) {
		npcList.add(npc);
	}
	
	public void removeNpc(Npc npc) {
		npcList.remove(npc);
	}
	
	public List<Player> getPlayerList() {
		return Collections.unmodifiableList(playerList);
	}
	
	public List<Npc> getNpcList() {
		return Collections.unmodifiableList(npcList);
	}
	
	public boolean isEmpty() {
		return playerList.isEmpty() && npcList.isEmpty();
	}
}
