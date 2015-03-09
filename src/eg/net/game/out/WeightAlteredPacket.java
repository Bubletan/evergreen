package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class WeightAlteredPacket implements AbstractGamePacket {
    
    private final int weight;
    
    public WeightAlteredPacket(int weight) {
        this.weight = weight;
    }
    
    public int getWeight() {
        return weight;
    }
}
