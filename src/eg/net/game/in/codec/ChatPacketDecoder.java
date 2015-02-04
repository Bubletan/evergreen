package eg.net.game.in.codec;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.ChatPacket;
import eg.util.io.Buffer;

public final class ChatPacketDecoder implements AbstractGamePacketDecoder<ChatPacket> {
	
	private static final char[] XLATE_TABLE = {
		' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c',
		'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&',
		'*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']'
	};
	
	@Override
	public ChatPacket decode(GamePacket packet) throws Exception {
		Buffer buf = packet.toBuffer();
		int animEffect = buf.getUByte3();
		int colorEffect = buf.getUByte3();
		int size = packet.getSize() - 2;
		byte[] bytes = buf.getBytesReversely(null, 0, size);
		for (int i = 0, len = bytes.length; i < len; i++) {
			bytes[i] = (byte) (bytes[i] - 128);
		}
		String unpacked = textUnpack(bytes, size);
		unpacked = filterText(unpacked);
		unpacked = optimizeText(unpacked);
		byte[] packed = new byte[size];
		textPack(packed, unpacked);
		return new ChatPacket(animEffect, colorEffect, unpacked, packed);
	}
	
	public static String textUnpack(byte packedData[], int size) {
		byte[] decodeBuf = new byte[4096];
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
			if (highNibble == -1) {
				if (val < 13) {
					decodeBuf[idx++] = (byte) XLATE_TABLE[val];
				} else {
					highNibble = val;
				}
			} else {
				decodeBuf[idx++] = (byte) XLATE_TABLE[((highNibble << 4) + val) - 195];
				highNibble = -1;
			}
		}
		return new String(decodeBuf, 0, idx);
	}

	public static String optimizeText(String text) {
		char buf[] = text.toCharArray();
		boolean endMarker = true;
		for (int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if (endMarker && c >= 'a' && c <= 'z') {
				buf[i] -= 0x20;
				endMarker = false;
			}
			if (c == '.' || c == '!' || c == '?') {
				endMarker = true;
			}
		}
		return new String(buf, 0, buf.length);
	}

	public static void textPack(byte packedData[], String text) {
		if (text.length() > 80) {
			text = text.substring(0, 80);
		}
		text = text.toLowerCase();
		int carryOverNibble = -1;
		int ofs = 0;
		for (int idx = 0; idx < text.length(); idx++) {
			char c = text.charAt(idx);
			int tableIdx = 0;
			for (int i = 0; i < XLATE_TABLE.length; i++) {
				if (c == (byte) XLATE_TABLE[i]) {
					tableIdx = i;
					break;
				}
			}
			if (tableIdx > 12) {
				tableIdx += 195;
			}
			if (carryOverNibble == -1) {
				if (tableIdx < 13) {
					carryOverNibble = tableIdx;
				} else {
					packedData[ofs++] = (byte) (tableIdx);
				}
			} else if (tableIdx < 13) {
				packedData[ofs++] = (byte) ((carryOverNibble << 4) + tableIdx);
				carryOverNibble = -1;
			} else {
				packedData[ofs++] = (byte) ((carryOverNibble << 4) + (tableIdx >> 4));
				carryOverNibble = tableIdx & 0xf;
			}
		}
		if (carryOverNibble != -1) {
			packedData[ofs++] = (byte) (carryOverNibble << 4);
		}
	}

	public static String filterText(String s) {
		StringBuilder bldr = new StringBuilder();
		for (char c : s.toLowerCase().toCharArray()) {
			boolean valid = false;
			for (char validChar : XLATE_TABLE) {
				if (validChar == c) {
					valid = true;
				}
			}
			if (valid) {
				bldr.append(c);
			}
		}
		return bldr.toString();
	}
}
