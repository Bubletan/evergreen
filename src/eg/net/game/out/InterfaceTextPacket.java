package eg.net.game.out;

import java.util.Objects;

import eg.net.game.AbstractGamePacket;

public final class InterfaceTextPacket implements AbstractGamePacket {
    
    private final int id;
    private final String text;
    
    public InterfaceTextPacket(int id, String text) {
        this.id = id;
        this.text = Objects.requireNonNull(text);
    }
    
    public int getId() {
        return id;
    }
    
    public String getText() {
        return text;
    }
}
