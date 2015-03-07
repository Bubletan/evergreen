package eg.util.io;

import java.util.LinkedList;
import java.util.Queue;

/**
 * An utility class that allows basic operations and recycling
 * for {@link Buffer Buffers}.
 * @author Rob / Bubletan
 */
public final class Buffers {
	
	private static final Queue<Buffer> pool = new LinkedList<>();
	
	private Buffers() {
	}
	
	/**
	 * Returns either a pooled or a new {@link Buffer} which has equal settings to
	 * one created by {@link Buffer#Buffer() new Buffer()} but an unknown size and data.
	 */
	public static Buffer allocate() {
		synchronized (pool) {
			if (!pool.isEmpty()) {
				return pool.remove();
			}
		}
		return new Buffer();
	}
	
	/**
	 * Releases a {@link Buffer} into the recycling pool to allow the same instance to be reused.
	 * At some point, this instance will be received by calling {@link Buffers#allocate()}.<br>
	 * <b>A buffer must not be accessed in any way after it has been released.</b>
	 */
	public static void release(Buffer buf) {
		if (buf.isBitAccess()) {
			buf.endBitAccess();
		}
		if (buf.isBlock()) {
			buf.endBlock();
		}
		buf.setPosition(0);
		buf.setAutoExpanding(true);
		synchronized (pool) {
			pool.add(buf);
		}
	}
	
	/**
	 * Clears the recycling pool.
	 */
	public static void reset() {
		synchronized (pool) {
			pool.clear();
		}
	}
}
