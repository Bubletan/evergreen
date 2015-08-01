package eg.net.game.in;

import java.util.Objects;

import eg.net.game.AbstractGamePacket;

public final class PublicChatMessagePacket implements AbstractGamePacket {
    
    private final int colorEffect;
    private final int animationEffect;
    private final byte[] encodedMessage;
    
    public PublicChatMessagePacket(int colorEffect, int animationEffect, byte[] encodedMessage) {
        this.animationEffect = animationEffect;
        this.colorEffect = colorEffect;
        this.encodedMessage = Objects.requireNonNull(encodedMessage);
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
