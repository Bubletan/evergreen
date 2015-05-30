package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class TabFlashPacket implements AbstractGamePacket {
    
    private final int tab;
    
    public TabFlashPacket(int tab) {
        this.tab = tab;
    }
    
    public int getTab() {
        return tab;
    }
}
