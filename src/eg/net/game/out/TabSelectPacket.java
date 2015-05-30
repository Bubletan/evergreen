package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class TabSelectPacket implements AbstractGamePacket {
    
    private final int tab;
    
    public TabSelectPacket(int tab) {
        this.tab = tab;
    }
    
    public int getTab() {
        return tab;
    }
}
