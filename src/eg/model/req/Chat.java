package eg.model.req;

public final class Chat {

	private int color;
	private int animation;
	private int privilege;
	private String message;
	
	public Chat(int color, int animation, int privilege, String message) {
		this.color = color;
		this.animation = animation;
		this.privilege = privilege;
		this.message = message;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getAnimation() {
		return animation;
	}
	
	public int getPrivilege() {
		return privilege;
	}
	
	public String getMessage() {
		return message;
	}
}
