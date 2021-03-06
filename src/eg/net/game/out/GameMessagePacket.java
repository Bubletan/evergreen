package eg.net.game.out;

import java.util.Objects;

import eg.net.game.AbstractGamePacket;

public final class GameMessagePacket implements AbstractGamePacket {
    
    private final String sender;
    private final String message;
    private final Type type;
    
    public GameMessagePacket(String message) {
        this("", message, Type.DEFAULT);
    }
    
    public GameMessagePacket(String sender, String message, Type type) {
        this.sender = Objects.requireNonNull(sender);
        this.message = Objects.requireNonNull(message);
        this.type = Objects.requireNonNull(type);
    }
    
    public String getSender() {
        return sender;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Type getType() {
        return type;
    }
    
    public static enum Type {
        
        DEFAULT, TRADE_REQUEST, DUEL_REQUEST, CHALLENGE_REQUEST
    }
}
