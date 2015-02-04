package eg.model.req;

public final class ForcedChat {

	private String message;
	
	public ForcedChat(String message) {
		this(message, false);
	}
	
	public ForcedChat(String message, boolean showInChatbox) {
		if (showInChatbox) {
			message = '~' + message;
		}
		this.message = message;
	}
	
	public final String getMessage() {
		return message;
	}
}
