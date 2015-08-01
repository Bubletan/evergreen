package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class PrivateChatMessagePacket implements AbstractGamePacket {
    
    private final long recipient;
    private final byte[] encodedMessage;
    
    public PrivateChatMessagePacket(long recipient, byte[] encodedMessage) {
        this.recipient = recipient;
        this.encodedMessage = encodedMessage;
    }
    
    public long getRecipient() {
        return recipient;
    }
    
    public byte[] getEncodedMessage() {
        return encodedMessage;
    }
}
