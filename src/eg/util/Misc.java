package eg.util;

import java.security.SecureRandom;
import java.util.Random;

public final class Misc {
    
    private Misc() {
    }
    
    private static final Random random = new Random();
    
    /**
     * Returns a random {@code int} between {@code 0} and {@code range - 1}.
     */
    public static int random(int range) {
        return random.nextInt(range);
    }
    
    private static final SecureRandom secureRandom = new SecureRandom();
    
    /**
     * Returns a random {@code int} between {@code 0} and {@code range - 1}.
     */
    public static int secureRandom(int range) {
        return secureRandom.nextInt(range);
    }
    
    public static int getExperienceForLevel(int lvl) {
        if (lvl <= 0) {
            throw new IllegalArgumentException("Level out of range: " + lvl);
        }
        int n = 0;
        while (--lvl > 0) {
            n += (int) (lvl + 300 * Math.pow(2, lvl / 7f));
        }
        return n >> 2;
    }
    
    public static int getLevelForExperience(int exp) {
        if (exp < 0 || exp > 200_000_000) {
            throw new IllegalArgumentException("Experience out of range: "
                    + exp);
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
    
    public static long encryptUsername(String username) {
        long l = 0L;
        for (int i = 0; i < username.length() && i < 12; i++) {
            final char c = username.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z') {
                l += 1 + c - 65;
            } else if (c >= 'a' && c <= 'z') {
                l += 1 + c - 97;
            } else if (c >= '0' && c <= '9') {
                l += 27 + c - 48;
            }
        }
        for (; l % 37L == 0L && l != 0L; l /= 37L) {
        }
        return l;
    }
    
    private static final char[] USERNAME_CHAR_TABLE = {
        '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
        't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
        '3', '4', '5', '6', '7', '8', '9'
    };
    
    public static String decryptUsername(long l) {
        if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L) {
            return "invalid_name";
        }
        if (l % 37L == 0L) {
            return "invalid_name";
        }
        int i = 0;
        final char ac[] = new char[12];
        while (l != 0L) {
            final long l1 = l;
            l /= 37L;
            ac[11 - i++] = USERNAME_CHAR_TABLE[(int) (l1 - l * 37L)];
        }
        return new String(ac, 12 - i, i);
    }
}
