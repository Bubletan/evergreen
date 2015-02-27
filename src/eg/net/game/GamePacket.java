package eg.net.game;

import eg.util.io.Buffer;

public final class GamePacket {
	
	private int type;
	private byte[] data;
	private int size;
	
	public GamePacket(int type) {
		this(type, null);
	}
	
	public GamePacket(int type, byte[] data) {
		this(type, data, data != null ? data.length : 0);
	}
	
	public GamePacket(int type, byte[] data, int size) {
		if (type < 0 || type > 0xff) {
			throw new IllegalArgumentException("Packet type out of range: " + type);
		}
		if (size < 0) {
			throw new IllegalArgumentException("Size must not be negative: " + size);
		}
		if (size != 0) {
			if (data == null) {
				throw new IllegalArgumentException("Data may not be null if size is not zero.");
			} else if (size > data.length) {
				throw new IllegalArgumentException("Size must not be more than data length.");
			}
		}
		this.type = type;
		this.data = data;
		this.size = size;
	}
	
	public int getType() {
		return type;
	}
	
	public int getSize() {
		return size;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public Buffer toBuffer() {
		return new Buffer(data);
	}
	
	public String toDataString() {
		if (size != 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < size; i++) {
				String s = Integer.toHexString(data[i] & 0xff).toUpperCase();
				sb.append(s.length() == 1 ? '0' + s : s);
			}
			return sb.toString();
		} else {
			return "null";
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [type=" + type + ", data=" + toDataString() + "]";
	}
}
