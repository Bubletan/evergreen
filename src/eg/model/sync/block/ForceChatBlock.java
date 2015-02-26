package eg.model.sync.block;

import eg.model.req.ForceChat;
import eg.model.sync.SyncBlock;

public final class ForceChatBlock extends SyncBlock {
	
	private final ForceChat chat;
	
	public ForceChatBlock(ForceChat chat) {
		this.chat = chat;
	}
	
	public ForceChat getChat() {
		return chat;
	}
}
