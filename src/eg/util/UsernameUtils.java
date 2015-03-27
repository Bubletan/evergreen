package eg.util;

public final class UsernameUtils {
    
    private static final char[] USERNAME_CHAR_TABLE = {
        '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
        't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
        '3', '4', '5', '6', '7', '8', '9'
    };
    
    private UsernameUtils() {
    }
    
    public static long encrypt(String username) {
        long l = 0L;
        for (int i = 0; i < username.length() && i < 12; i++) {
            char c = username.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z') {
                l += 1 + c - 65;
            } else if (c >= 'a' && c <= 'z') {
                l += 1 + c - 97;
            } else if (c >= '0' && c <= '9') {
                l += 27 + c - 48;
            }
        }
        for (; l % 37L == 0L && l != 0L; l /= 37L);
        return l;
    }
    
    public static String decrypt(long hash) {
        if (hash <= 0L || hash >= 0x5b5b57f8a98a5dd1L) {
            return "invalid_name";
        }
        if (hash % 37L == 0L) {
            return "invalid_name";
        }
        int i = 0;
        char[] ac = new char[12];
        while (hash != 0L) {
            long l1 = hash;
            hash /= 37L;
            ac[11 - i++] = USERNAME_CHAR_TABLE[(int) (l1 - hash * 37L)];
        }
        return new String(ac, 12 - i, i);
    }
}
