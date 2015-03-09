package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class WindowFocusAlteredPacket implements AbstractGamePacket {
    
    private final boolean focused;
    
    public WindowFocusAlteredPacket(boolean focused) {
        this.focused = focused;
    }
    
    public boolean isFocused() {
        return focused;
    }
}
