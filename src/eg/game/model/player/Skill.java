package eg.game.model.player;

import eg.Config;
import eg.util.ExperienceUtils;

public final class Skill {
    
    private static final int MAX_XP = 200_000_000;
    private static final int MAX_XP_10 = MAX_XP * 10;
    
    private int xp10;
    private int level;
    private int pseudoLevel;
    
    public double getExperience() {
        return xp10 / 10d;
    }
    
    public void addExperience(double n) {
        addExperienceExact(n * Config.XP_MULTIPLIER);
    }
    
    public void addExperienceExact(double n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        int n10 = (int) (n * 10);
        if (xp10 <= MAX_XP_10 - n10) {
            xp10 += n10;
        } else if (xp10 != MAX_XP_10) {
            xp10 = MAX_XP_10;
        } else {
            return;
        }
        int newLevel = ExperienceUtils.experienceToLevel(xp10 / 10);
        if (level != newLevel) {
            pseudoLevel += newLevel - level;
            level = newLevel;
        }
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getPseudoLevel() {
        return pseudoLevel;
    }
    
    public void addPseudoLevel(int n) {
        pseudoLevel += n;
        if (pseudoLevel < 0) {
            pseudoLevel = 0;
        }
    }
}
