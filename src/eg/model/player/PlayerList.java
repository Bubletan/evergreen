package eg.model.player;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class PlayerList implements Iterable<Player> {
	
	private final int size;
	private final List<Player> list = new LinkedList<>();
	
	public PlayerList(int size) {
		this.size = size;
	}
	
	public boolean add(Player player) {
		if (player == null) {
			throw new IllegalArgumentException("Player must not be null.");
		}
		if (list.size() >= size) {
			return false;
		}
		if (list.contains(player)) {
			return false;
		}
		list.add(player);
		return true;
	}
	
	public int size() {
		return list.size();
	}
	
	public boolean remove(Player player) {
		return list.remove(player);
	}
	
	public boolean contains(Player player) {
		return list.contains(player);
	}

	@Override
	public Iterator<Player> iterator() {
		return list.iterator();
	}
}
