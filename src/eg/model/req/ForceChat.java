package eg.model.req;

public final class ForceChat {

	private final String message;
	private final boolean showInChatbox;
	
	public ForceChat(String message) {
		this(message, false);
	}
	
	public ForceChat(String message, boolean showInChatbox) {
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
