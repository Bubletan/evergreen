package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class TabInterfacePacket implements AbstractGamePacket {
    
    private final int tab;
    private final int id;
    
    public TabInterfacePacket(int tab, int id) {
        this.tab = tab;
        this.id = id;
    }
    
    public int getTab() {
        return tab;
    }
    
    public int getId() {
        return id;
    }
}
