package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class ChatMessagePacket implements AbstractGamePacket {
    
    private final int colorEffect;
    private final int animationEffect;
    private final byte[] encodedMessage;
    
    public ChatMessagePacket(int colorEffect, int animationEffect, byte[] encodedMessage) {
        this.animationEffect = animationEffect;
        this.colorEffect = colorEffect;
        this.encodedMessage = encodedMessage;
    }
    
    public int getColorEffect() {
        return colorEffect;
    }
    
    public int getAnimationEffect() {
        return animationEffect;
    }
    
    public byte[] getEncodedMessage() {
        return encodedMessage;
    }
}
