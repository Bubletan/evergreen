package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class AmountInputEnteredPacket implements AbstractGamePacket {
    
    private final int amount;
    
    public AmountInputEnteredPacket(int amount) {
        this.amount = amount;
    }
    
    public int getAmount() {
        return amount;
    }
}
