package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class CameraWavePacket implements AbstractGamePacket {
    
    private final Type type;
    private final int noise;
    private final int amplitude;
    private final int frequency;
    
    public CameraWavePacket(Type type, int noise, int amplitude, int frequency) {
        this.type = type;
        this.noise = noise;
        this.amplitude = amplitude;
        this.frequency = frequency;
    }
    
    public static enum Type {
        
        X(0), Y(1), HEIGHT(2), ROLL(3), YAW(4);
        
        private final int value;
        
        private Type(int value) {
            this.value = value;
        }
        
        public int toInt() {
            return value;
        }
    }
    
    public Type getType() {
        return type;
    }
    
    public int getNoise() {
        return noise;
    }
    
    public int getAmplitude() {
        return amplitude;
    }
    
    public int getFrequency() {
        return frequency;
    }
}
