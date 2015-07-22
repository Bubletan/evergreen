package eg.net.game.out;

import eg.net.game.AbstractGamePacket;

public final class SkillAlteredPacket implements AbstractGamePacket {
    
    private final int id;
    private final int experience;
    private final int level;
    
    public SkillAlteredPacket(int id, int experience, int level) {
        this.id = id;
        this.experience = experience;
        this.level = level;
    }
    
    public int getId() {
        return id;
    }
    
    public int getExperience() {
        return experience;
    }
    
    public int getLevel() {
        return level;
    }
}
