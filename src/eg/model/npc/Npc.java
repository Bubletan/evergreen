package eg.model.npc;

import eg.model.Charactor;

public final class Npc extends Charactor {

	private final NpcType type;
	
	public Npc(NpcType type) {
		this.type = type;
	}
	
	public NpcType getType() {
		return type;
	}
}
