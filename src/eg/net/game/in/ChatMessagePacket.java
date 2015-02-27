package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class ChatMessagePacket implements AbstractGamePacket {
	
	private final int animEffect;
	private final int colorEffect;
	private final String unpacked;
	private final byte[] packed;
	
	public ChatMessagePacket(int animEffect, int colorEffect, String unpacked,
			byte[] packed) {
		this.animEffect = animEffect;
		this.colorEffect = colorEffect;
		this.unpacked = unpacked;
		this.packed = packed;
	}
	
	public int getColorEffect() {
		return colorEffect;
	}
	
	public int getAnimationEffect() {
		return animEffect;
	}
	
	public byte[] getCompressedMessage() {
		return packed;
	}
}
