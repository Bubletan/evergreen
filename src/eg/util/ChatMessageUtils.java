package eg.util;

public final class ChatMessageUtils {
    
    private static final char[] CHAT_MESSAGE_CHAR_TABLE = {
        ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 
        'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 
        'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', 
        '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', 
        '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', 
        '\'', '@', '#', '+', '=', '£', '$', '%', '"', '[', 
        ']', '>', '<', '^', '/', '_'
    };
    
    private ChatMessageUtils() {
    }
    
    public static String decode(byte[] in) {
        char[] out = new char[100];
        int outp = 0;
        for (int i = 0, len = in.length; i < len; i++) {
            int inv = in[i] & 0xff;
            out[outp++] = CHAT_MESSAGE_CHAR_TABLE[inv];
        }
        boolean upper = true;
        for (int i = 0; i < outp; i++) {
            char c = out[i];
            if (upper && c >= 'a' && c <= 'z') {
                out[i] += '\uffe0';
                upper = false;
            }
            if (c == '.' || c == '!' || c == '?') {
                upper = true;
            }
        }
        return new String(out, 0, outp);
    }
    
    public static byte[] encode(String in) {
        if (in.length() > 80) {
            in = in.substring(0, 80);
        }
        byte[] out = new byte[in.length()];
        in = in.toLowerCase();
        int outp = 0;
        for (int i = 0; i < in.length(); i++) {
            char inv = in.charAt(i);
            int outv = 0;
            for (int inp = 0; inp < CHAT_MESSAGE_CHAR_TABLE.length; inp++) {
                if (inv != CHAT_MESSAGE_CHAR_TABLE[inp]) {
                    continue;
                }
                outv = inp;
                break;
            }
            out[outp++] = (byte) outv;
        }
        return out;
    }
}
