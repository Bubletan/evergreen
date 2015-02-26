package eg.model.req;

import eg.model.npc.Npc;
import eg.model.player.Player;

// TODO remove
public final class FocusCharactor {
		
	public static final FocusCharactor NULL = new FocusCharactor(0xffff);

	private final int target;
	
	public FocusCharactor(Npc npc) {
		this(npc.getIndex());
	}
		
	public FocusCharactor(Player player) {
		this(player.getIndex() + 0x8000);
	}
		
	public FocusCharactor(int target) {
		this.target = target;
	}
		
	public int getTarget() {
		return target;
	}
}
