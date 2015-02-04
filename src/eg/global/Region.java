package eg.global;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eg.model.npc.Npc;
import eg.model.player.Player;

public final class Region {
	
	private static Map<Integer, Region> regionMap = new HashMap<Integer, Region>();

	private List<Player> playerList = new LinkedList<>();
	private List<Npc> npcList = new LinkedList<>();
	
	public static Region getRegion(int x, int y) {
		if (x < 0 || x > 0xffff || y < 0 || y > 0xffff) {
			throw new IllegalArgumentException("X and Y must be between 0 and 0xffff.");
		}
		final int hash = x << 16 | y;
		Region region = regionMap.get(hash);
		if (region == null) {
			region = new Region();
			regionMap.put(hash, region);
		}
		return region;
	}
	
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
}
