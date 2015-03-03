package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class ChatMessagePacket implements AbstractGamePacket {
	
	private final int animEffect;
	private final int colorEffect;
	private final byte[] encodedMessage;
	
	public ChatMessagePacket(int colorEffect, int animationEffect, byte[] encodedMessage) {
		this.animEffect = animationEffect;
		this.colorEffect = colorEffect;
		this.encodedMessage = encodedMessage;
	}
	
	public int getColorEffect() {
		return colorEffect;
	}
	
	public int getAnimationEffect() {
		return animEffect;
	}
	
	public byte[] getEncodedMessage() {
		return encodedMessage;
	}
}
