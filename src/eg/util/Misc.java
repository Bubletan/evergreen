package eg.util;

import java.security.SecureRandom;

public final class Misc {
	
	private Misc() {
	}
	
	/**
	 * Returns a random {@code int} between {@code 0} and {@code range - 1}.
	 */
	public static int random(int range) {
		return (int) (range * Math.random());
	}
	
	private static final SecureRandom secureRandom = new SecureRandom();
	
	/**
	 * Returns a random {@code int} between {@code 0} and {@code range - 1}.
	 */
	public static int secureRandom(int range) {
		return secureRandom.nextInt(range);
	}
	
	public static final int getExperienceForLevel(int lvl) {
		int n = 0;
		while (--lvl > 0) {
			n += Math.floor(lvl + 300f * Math.pow(2, lvl / 7f));
		}
		return n >> 2;
	}

	// TODO: Improve?
	public static final int getLevelForExperience(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430) {
			return 99;
		}
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2, lvl / 7f));
			output = (int) Math.floor(points >> 2);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
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
