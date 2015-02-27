package eg.global;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eg.Config;
import eg.model.Coordinate;
import eg.model.npc.Npc;
import eg.model.player.Player;

public final class World {
	
	private static final World INSTANCE = new World();
	
	private final Map<Integer, WorldSector> sectorMap = new HashMap<>();
	
	private final Player[] playerListLive = new Player[Config.MAX_PLAYERS];
	private int playerCountLive;
	private final Map<Long, Player> playerForHash = new HashMap<>();
	
	private final Npc[] npcListLive = new Npc[Config.MAX_NPCS];
	private int npcCountLive;
	
	private List<Player> playerList;
	private List<Npc> npcList;
	
	private World() {
	}
	
	public static World getWorld() {
		return INSTANCE;
	}
	
	public void updateLists() {
		
		List<Player> players = Arrays.stream(playerListLive).parallel()
				.filter(p -> p != null).collect(Collectors.toList());
		List<Npc> npcs = Arrays.stream(npcListLive).parallel()
				.filter(n -> n != null).collect(Collectors.toList());
		
		playerList = Collections.unmodifiableList(players);
		npcList = Collections.unmodifiableList(npcs);
		
		playerList.stream().forEach(player -> {
			WorldSector sector = getOrCreateWorldSector(player.getCoordinate());
			if (sector != player.getWorldSector()) {
				player.getWorldSector().removePlayer(player);
				sector.addPlayer(player);
				player.setWorldSector(sector);
			}
		});
		
		npcList.stream().forEach(npc -> {
			WorldSector sector = getOrCreateWorldSector(npc.getCoordinate());
			if (sector != npc.getWorldSector()) {
				npc.getWorldSector().removeNpc(npc);
				sector.addNpc(npc);
				npc.setWorldSector(sector);
			}
		});
	}
	
	public boolean addPlayer(Player player) {
		if (playerCountLive >= Config.MAX_PLAYERS) {
			return false;
		}
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (playerListLive[i] == null) {
				playerListLive[i] = player;
				playerCountLive++;
				playerForHash.put(player.getHash(), player);
				player.setIndex(i + 1);
				WorldSector sector = getOrCreateWorldSector(player.getCoordinate());
				sector.addPlayer(player);
				player.setWorldSector(sector);
				player.initialize();
				return true;
			}
		}
		return false;
	}
	
	public boolean removePlayer(Player player) {
		int index = player.getIndex() - 1;
		if (playerListLive[index] != player) {
			return false;
		}
		playerListLive[index] = null;
		playerCountLive--;
		playerForHash.remove(player.getHash());
		WorldSector sector = player.getWorldSector();
		sector.removePlayer(player);
		return true;
	}
	
	public Player getPlayerForHash(long hash) {
		return playerForHash.get(hash);
	}
	
	public List<Player> getPlayerList() {
		return playerList;
	}
	
	public int getPlayerCount() {
		return playerList.size();
	}
	
	public boolean addNpc(Npc npc) {
		if (npcCountLive >= Config.MAX_NPCS) {
			return false;
		}
		for (int i = 0; i < Config.MAX_NPCS; i++) {
			if (npcListLive[i] == null) {
				npcListLive[i] = npc;
				npcCountLive++;
				npc.setIndex(i + 1);
				WorldSector sector = getOrCreateWorldSector(npc.getCoordinate());
				sector.addNpc(npc);
				npc.setWorldSector(sector);
				return true;
			}
		}
		return false;
	}
	
	public boolean removeNpc(Npc npc) {
		int index = npc.getIndex() - 1;
		if (npcListLive[index] != npc) {
			return false;
		}
		npcListLive[index] = null;
		npcCountLive--;
		WorldSector sector = npc.getWorldSector();
		sector.removeNpc(npc);
		return true;
	}
	
	public List<Npc> getNpcList() {
		return npcList;
	}
	
	public int getNpcCount() {
		return npcList.size();
	}
	
	private WorldSector getOrCreateWorldSector(Coordinate coord) {
		int x = coord.getX() >> 3;
		int y = coord.getY() >> 3;
		int height = coord.getHeight();
		int hash = height << 30 | x << 15 | y;
		WorldSector sector = sectorMap.get(hash);
		if (sector == null) {
			sector = new WorldSector();
			sectorMap.put(hash, sector);
		}
		return sector;
	}
}
