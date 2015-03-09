package eg.game.model.player;

import eg.Config;
import eg.util.Misc;

public final class Skill {
    
    private static final int MAX_EXP = 200_000_000;
    private static final int MAX_EXP_TMS_10 = 10 * MAX_EXP;
    
    private int expTimes10;
    private int lvl;
    private int pseudoLvl = 1;
    
    public final int getExperience() {
        return expTimes10 / 10;
    }
    
    /**
     * Sets the experience by an {@code int} that has a value of ten times the
     * experience wanted.
     */
    protected final void setExperience10(int n) {
        expTimes10 = n;
        if (expTimes10 > MAX_EXP_TMS_10) {
            expTimes10 = MAX_EXP_TMS_10;
        } else if (expTimes10 < 0) {
            expTimes10 = 0;
        }
        lvl = Misc.getLevelForExperience(expTimes10 / 10);
    }
    
    public final void addExperience(float n) {
        setExperience10((int) (10 * n));
    }
    
    public final void addTrainedExperience(float n) {
        addExperience(n * Config.EXP_MULTIPLIER);
    }
    
    public final int getLevel() {
        return lvl;
    }
    
    public final int getPseudoLevel() {
        return pseudoLvl;
    }
    
    public void setPseudoLevel(int n) {
        pseudoLvl = n;
        if (pseudoLvl < 0) {
            pseudoLvl = 0;
        }
    }
    
    public void addPseudoLevel(int n) {
        setPseudoLevel(pseudoLvl + n);
    }
}
