package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class EnergyAlteredPacket implements AbstractGamePacket {
    
    private final int energy;
    
    public EnergyAlteredPacket(int energy) {
        this.energy = energy;
    }
    
    public int getEnergy() {
        return energy;
    }
}
