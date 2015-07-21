package eg.net.game.out;

import java.util.Objects;

import eg.net.game.AbstractGamePacket;
import eg.util.Color;

public final class InterfaceColorPacket implements AbstractGamePacket {
    
    private final int id;
    private final Color color;
    
    public InterfaceColorPacket(int id, Color color) {
        this.id = id;
        this.color = Objects.requireNonNull(color);
    }
    
    public int getId() {
        return id;
    }
    
    public Color getColor() {
        return color;
    }
}
