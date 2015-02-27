package eg.model.req;

public final class ForceChatMessage {

	private final String message;
	private final boolean showInChatbox;
	
	public ForceChatMessage(String message) {
		this(message, false);
	}
	
	public ForceChatMessage(String message, boolean showInChatbox) {
		this.message = message;
		this.showInChatbox = showInChatbox;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean showInChatbox() {
		return showInChatbox;
	}
}
