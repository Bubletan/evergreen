package eg.model.sync.block;

import eg.model.req.Chat;
import eg.model.sync.SyncBlock;

public final class ChatBlock extends SyncBlock {
	
	private final Chat chat;
	
	public ChatBlock(Chat chat) {
		this.chat = chat;
	}
	
	public Chat getChat() {
		return chat;
	}
}
