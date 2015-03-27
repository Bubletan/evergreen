package eg.util;

public final class ExperienceUtils {
    
    private ExperienceUtils() {
    }
    
    public static int levelToExperience(int lvl) {
        if (lvl <= 0 || lvl > 99) {
            throw new IllegalArgumentException("Level out of range: " + lvl);
        }
        int n = 0;
        while (--lvl > 0) {
            n += (int) (lvl + 300 * Math.pow(2, lvl / 7f));
        }
        return n >> 2;
    }
    
    public static int experienceToLevel(int exp) {
        if (exp < 0 || exp > 200_000_000) {
            throw new IllegalArgumentException("Experience out of range: " + exp);
        }
        if (exp < 83) {
            return 1;
        }
        if (exp >= 13_034_431) {
            return 99;
        }
        int n = 0, lvl = 0, exp4 = exp << 2;
        while (n < exp4) {
            n += (int) (++lvl + 300 * Math.pow(2, lvl / 7f));
        }
        return lvl;
    }
}
