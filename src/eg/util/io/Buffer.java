package eg.util.io;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A buffer that allows input and output, bit access system and a cipher that
 * can be bound to encrypt the packet opcodes.<br>
 * <br>
 * By default the data is put and got in a big-endian order and got as signed
 * values. Using the optional methods, it is possible to use different byte
 * orders and get unsigned values.
 * 
 * @author Rob / Bubletan
 */
public final class Buffer {
    
    /**
     * Used for empty {@link Buffer}s that no longer contain any data.
     */
    private static final byte[] EMPTY = {};
    
    /**
     * Bit mask array used for bit based input and output.<br>
     * {@code BIT_MASK[i] = (1 << i) - 1}
     */
    private static final int[] BIT_MASK = {
        0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767,
        65535, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
        0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1
    };
    
    /**
     * Recycling pool for unaccessed {@code byte[]}s.
     */
    private static final Queue<byte[]> pool = new LinkedList<>();
    
    private byte[] data;
    private int pos;
    private boolean bitAccess;
    private boolean autoExpand;
    private int blockBegin;
    private int blockType;
    private boolean poolable;
    
    /**
     * Initializes a new {@link Buffer} either by using a pooled backing array with an unknown capacity
     * and data or by allocating space of the default capacity (16).
     * Automatic expanding is <b>enabled</b> by default.
     */
    public Buffer() {
        data = poolGet();
        autoExpand = poolable = true;
    }
    
    /**
     * Initializes a new {@link Buffer} by allocating space of the required
     * capacity. Automatic expanding is <b>disabled</b> by default.
     */
    public Buffer(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must not be negative.");
        }
        data = new byte[capacity];
        poolable = true;
    }
    
    /**
     * Initializes a new {@link Buffer} by wrapping the given {@code byte[]}.
     * Automatic expanding is <b>disabled</b> by default.
     */
    public Buffer(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null.");
        }
        this.data = data;
    }
    
    /**
     * Gets the current position as the index of the byte.
     */
    public int getPosition() {
        return bitAccess ? (pos + 7) >> 3 : pos;
    }
    
    /**
     * Gets the current position as the index of the bit.
     */
    public int getBitPosition() {
        return bitAccess ? pos : pos << 3;
    }
    
    /**
     * Sets the current position based on the index of the byte.<br>
     * Returns itself to allow method chaining.
     */
    public Buffer setPosition(int value) {
        pos = bitAccess ? value << 3 : value;
        return this;
    }
    
    /**
     * Sets the current position based on the index of the bit.<br>
     * Returns itself to allow method chaining.
     */
    public Buffer setBitPosition(int value) {
        pos = bitAccess ? value : (value + 7) >> 3;
        return this;
    }
    
    /**
     * Shifts the current position based on the amount of bytes.<br>
     * Returns itself to allow method chaining.
     */
    public Buffer shiftPosition(int n) {
        pos += bitAccess ? n << 3 : n;
        return this;
    }
    
    /**
     * Shifts the current position based on the amount of bits.<br>
     * Returns itself to allow method chaining.
     */
    public Buffer shiftBitPosition(int n) {
        pos += bitAccess ? n : (n + 7) >> 3;
        return this;
    }
    
    /**
     * Shifts the current position based on the amount of bytes and gets the new
     * position as the index of the byte.
     */
    public int shiftAndGetPosition(int n) {
        shiftPosition(n);
        return getPosition();
    }
    
    /**
     * Shifts the current position based on the amount of bytes and gets the old
     * position as the index of the byte.
     */
    public int getAndShiftPosition(int n) {
        int tmp = getPosition();
        shiftPosition(n);
        return tmp;
    }
    
    /**
     * Shifts the current position based on the amount of bits and gets the new
     * position as the index of the bit.
     */
    public int shiftAndGetBitPosition(int n) {
        shiftBitPosition(n);
        return getBitPosition();
    }
    
    /**
     * Shifts the current position based on the amount of bits and gets the old
     * position as the index of the bit.
     */
    public int getAndShiftBitPosition(int n) {
        int tmp = getBitPosition();
        shiftBitPosition(n);
        return tmp;
    }
    
    /**
     * Gets the maximum amount of bytes this buffer can hold.
     */
    public int getCapacity() {
        return data.length;
    }
    
    /**
     * Gets the maximum amount of bits this buffer can hold.
     */
    public int getBitCapacity() {
        return data.length << 3;
    }
    
    /**
     * Sets the capacity to a certain amount of bytes.
     * If the new capacity is smaller than the previous one, there will be lost data.<br>
     * Returns itself to allow method chaining.
     */
    public Buffer setCapacity(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Capacity may not be negative.");
        }
        if (value != data.length) {
            if (poolable) {
                poolAdd(data);
            } else {
                poolable = true;
            }
            byte[] newData = new byte[value];
            System.arraycopy(data, 0, newData, 0, Math.min(data.length, value));
            data = newData;
        }
        return this;
    }
    
    /**
     * Sets the capacity to a certain amount of bits rounded up to the next byte.
     * If the new capacity is smaller than the previous one, there will be lost data.<br>
     * Returns itself to allow method chaining.
     */
    public Buffer setBitCapacity(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Capacity may not be negative.");
        }
        return setCapacity((value + 7) >> 3);
    }
    
    /**
     * Returns the amount of bytes remaining in this buffer.
     */
    public int getRemaining() {
        return getCapacity() - getPosition();
    }
    
    /**
     * Returns the amount of bits remaining in this buffer.
     */
    public int getBitRemaining() {
        return getBitCapacity() - getBitPosition();
    }
    
    /**
     * TODO document
     */
    public void releaseData() {
        if (data != EMPTY) {
            if (poolable) {
                poolAdd(data);
                poolable = false;
            }
            data = EMPTY;
        }
    }
    
    /**
     * Returns a {@code byte[]} representing the data. The backing
     * array will be replaced by an empty array and the new capacity
     * will be zero.
     */
    public byte[] toData() {
        byte[] tmp = data;
        data = EMPTY;
        if (poolable) {
            poolable = false;
        }
        return tmp;
    }
    
    /**
     * Returns a {@code byte[]} representing the data from the
     * position zero to the current position. he backing array will be
     * replaced by an empty array and the new capacity will be zero.
     */
    public byte[] toShrunkData() {
        int size = getPosition();
        if (size == data.length) {
            return toData();
        } else {
            byte[] copy = new byte[size];
            System.arraycopy(data, 0, copy, 0, size);
            if (poolable) {
                poolAdd(data);
                poolable = false;
            }
            data = EMPTY;
            return copy;
        }
    }
    
    /**
     * Sets the automatic expanding of the capacity enabled or disabled.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #isAutoExpand()
     */
    public Buffer setAutoExpand(boolean enabled) {
        autoExpand = enabled;
        return this;
    }
    
    /**
     * Checks if the automatic expanding of the capacity is enabled.
     * 
     * @see #setAutoExpand(boolean)
     */
    public boolean isAutoExpand() {
        return autoExpand;
    }
    
    private void requirePuttingCapacity(int n) {
        if (!isPuttingPositionAvailable(pos + n - 1)) {
            throw new IndexOutOfBoundsException("Buffer overflow: " + (pos + n - 1));
        }
    }
    
    private void requireBitPuttingCapacity(int n) {
        if (!isPuttingPositionAvailable((pos + n - 1) >> 3)) {
            throw new IndexOutOfBoundsException("Buffer overflow: " + ((pos + n - 1) >> 3));
        }
    }
    
    /**
     * Checks if the the required position is available in the buffer.<br>
     * Expands the buffer capacity when needed if automatic expanding is
     * enabled.
     */
    private boolean isPuttingPositionAvailable(int pos) {
        if (data == null) {
            return false;
        }
        if (pos < data.length) {
            return true;
        }
        if (!autoExpand) {
            return false;
        }
        int newCapacity = data.length * 3 / 2;
        if (pos >= newCapacity) {
            newCapacity = pos * 4 / 3;
        }
        byte[] newData = new byte[newCapacity];
        System.arraycopy(data, 0, newData, 0, data.length);
        if (poolable) {
            poolAdd(data);
        } else {
            poolable = true;
        }
        data = newData;
        return true;
    }
    
    private void requireGettingCapacity(int n) {
        if (pos + n - 1 >= data.length) {
            throw new IndexOutOfBoundsException("Buffer overflow: " + (pos + n - 1));
        }
    }
    
    private void requireBitGettingCapacity(int n) {
        if ((pos + n - 1) >> 3 >= data.length) {
            throw new IndexOutOfBoundsException("Buffer overflow: " + ((pos + n - 1) >> 3));
        }
    }
    
    private void requireBitAccess(boolean state) {
        if (bitAccess != state) {
            throw new IllegalStateException("Bit access is " + (bitAccess ? "enabled" : "disabled") + ".");
        }
    }
    
    private void checkByteArray(byte[] array, int length) {
        if (array == null) {
            throw new NullPointerException("Array must not be null.");
        }
        if (length > array.length) {
            throw new ArrayIndexOutOfBoundsException("Array is not long enough: " + length);
        }
    }
    
    private static void poolAdd(byte[] data) {
        synchronized (pool) {
            if (pool.size() < 100) {
                pool.add(data);
            }
        }
    }
    
    private static byte[] poolGet() {
        synchronized (pool) {
            if (!pool.isEmpty()) {
                return pool.remove();
            }
        }
        return new byte[16];
    }
    
    /**
     * Begins a block with an unsigned 8 bit size variable header.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #endBlock()
     * @see #isBlock()
     */
    public Buffer beginUByteBlock() {
        requireBitAccess(false);
        if (blockType != 0) {
            throw new IllegalStateException("Blocks may not be nested.");
        }
        blockType = 1;
        blockBegin = ++pos;
        return this;
    }
    
    /**
     * Begins a block with an unsigned 8 bit size variable header with an
     * opposite sign.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #endBlock()
     * @see #isBlock()
     */
    public Buffer beginNegatedUByteBlock() {
        requireBitAccess(false);
        if (blockType != 0) {
            throw new IllegalStateException("Blocks may not be nested.");
        }
        blockType = 2;
        blockBegin = ++pos;
        return this;
    }
    
    /**
     * Begins a block with an unsigned big-endian 16 bit size variable header.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #endBlock()
     * @see #isBlock()
     */
    public Buffer beginUShortBlock() {
        requireBitAccess(false);
        if (blockType != 0) {
            throw new IllegalStateException("Blocks may not be nested.");
        }
        blockType = 3;
        blockBegin = pos += 2;
        return this;
    }
    
    /**
     * Ends the current block.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #beginUByteBlock()
     * @see #beginNegatedUByteBlock()
     * @see #beginUShortBlock()
     * @see #isBlock()
     */
    public Buffer endBlock() {
        requireBitAccess(false);
        if (blockType == 0) {
            throw new IllegalStateException("No block exists.");
        }
        int blockSize = pos - blockBegin;
        if (blockSize < 0) {
            throw new RuntimeException("Block size must not be negative: " + blockSize);
        }
        if (blockType <= 2) {
            if (blockSize > 0xff) {
                throw new RuntimeException("Block size out of ubyte range: " + blockSize);
            }
            data[blockBegin - 1] = (byte) (blockType == 1 ? blockSize : -blockSize);
        } else {
            if (blockSize > 0xffff) {
                throw new RuntimeException("Block size out of ushort range: " + blockSize);
            }
            data[blockBegin - 2] = (byte) (blockSize >> 8);
            data[blockBegin - 1] = (byte) blockSize;
        }
        blockType = 0;
        return this;
    }
    
    /**
     * Returns {@code true} if an unended block exists, {@code false} otherwise.
     * 
     * @see #beginUByteBlock()
     * @see #beginNegatedUByteBlock()
     * @see #beginUShortBlock()
     * @see #endBlock()
     */
    public boolean isBlock() {
        return blockType != 0;
    }
    
    /**
     * Puts an {@code int} as an 8 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getByte()
     * @see #getUByte()
     */
    public Buffer putByte(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(1);
        data[pos++] = (byte) value;
        return this;
    }
    
    /**
     * Puts an {@code int} as an 8 bit value with negation data transformation.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getNegatedByte()
     * @see #getNegatedUByte()
     */
    public Buffer putNegatedByte(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(1);
        data[pos++] = (byte) -value;
        return this;
    }
    
    /**
     * Puts an {@code int} as an 8 bit value with subtraction data transformation.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getSubtractedByte()
     * @see #getSubtractedUByte()
     */
    public Buffer putSubtractedByte(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(1);
        data[pos++] = (byte) (128 - value);
        return this;
    }
    
    /**
     * Puts an {@code int} as an 8 bit value with addition data transformation.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getAddedByte()
     * @see #getAddedUByte()
     */
    public Buffer putAddedByte(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(1);
        data[pos++] = (byte) (value + 128);
        return this;
    }
    
    /**
     * Puts an {@code int} as a big-endian 16 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getShort()
     * @see #getUShort()
     */
    public Buffer putShort(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(2);
        data[pos++] = (byte) (value >> 8);
        data[pos++] = (byte) value;
        return this;
    }
    
    /**
     * Puts an {@code int} as a little-endian 16 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getLeShort()
     * @see #getLeUShort()
     */
    public Buffer putLeShort(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(2);
        data[pos++] = (byte) value;
        data[pos++] = (byte) (value >> 8);
        return this;
    }
    
    /**
     * Puts an {@code int} as a big-endian 16 bit value with addition data transformation.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getAddedShort()
     * @see #getAddedUShort()
     */
    public Buffer putAddedShort(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(2);
        data[pos++] = (byte) (value >> 8);
        data[pos++] = (byte) (value + 128);
        return this;
    }
    
    /**
     * Puts an {@code int} as a little-endian 16 bit value with addition data transformation.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getAddedLeShort()
     * @see #getAddedLeUShort()
     */
    public Buffer putAddedLeShort(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(2);
        data[pos++] = (byte) (value + 128);
        data[pos++] = (byte) (value >> 8);
        return this;
    }
    
    /**
     * Puts an {@code int} as a big-endian 24 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getMedium()
     * @see #getUMedium()
     */
    public Buffer putMedium(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(3);
        data[pos++] = (byte) (value >> 16);
        data[pos++] = (byte) (value >> 8);
        data[pos++] = (byte) value;
        return this;
    }
    
    /**
     * Puts an {@code int} as a little-endian 24 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getLeMedium()
     * @see #getLeUMedium()
     */
    public Buffer putLeMedium(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(3);
        data[pos++] = (byte) value;
        data[pos++] = (byte) (value >> 8);
        data[pos++] = (byte) (value >> 16);
        return this;
    }
    
    /**
     * Puts an {@code int} as a big-endian 32 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getInt()
     */
    public Buffer putInt(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(4);
        data[pos++] = (byte) (value >> 24);
        data[pos++] = (byte) (value >> 16);
        data[pos++] = (byte) (value >> 8);
        data[pos++] = (byte) value;
        return this;
    }
    
    /**
     * Puts an {@code int} as a little-endian 32 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getLeInt()
     */
    public Buffer putLeInt(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(4);
        data[pos++] = (byte) value;
        data[pos++] = (byte) (value >> 8);
        data[pos++] = (byte) (value >> 16);
        data[pos++] = (byte) (value >> 24);
        return this;
    }
    
    /**
     * Puts an {@code int} as a middle-endian 32 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getMeInt()
     */
    public Buffer putMeInt(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(4);
        data[pos++] = (byte) (value >> 16);
        data[pos++] = (byte) (value >> 24);
        data[pos++] = (byte) value;
        data[pos++] = (byte) (value >> 8);
        return this;
    }
    
    /**
     * Puts an {@code int} as a reversed middle-endian 32 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getRmeInt()
     */
    public Buffer putRmeInt(int value) {
        requireBitAccess(false);
        requirePuttingCapacity(4);
        data[pos++] = (byte) (value >> 8);
        data[pos++] = (byte) value;
        data[pos++] = (byte) (value >> 24);
        data[pos++] = (byte) (value >> 16);
        return this;
    }
    
    /**
     * Puts a {@code long} as a big-endian 64 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getLong()
     */
    public Buffer putLong(long value) {
        requireBitAccess(false);
        requirePuttingCapacity(8);
        data[pos++] = (byte) (int) (value >> 56);
        data[pos++] = (byte) (int) (value >> 48);
        data[pos++] = (byte) (int) (value >> 40);
        data[pos++] = (byte) (int) (value >> 32);
        data[pos++] = (byte) (int) (value >> 24);
        data[pos++] = (byte) (int) (value >> 16);
        data[pos++] = (byte) (int) (value >> 8);
        data[pos++] = (byte) (int) value;
        return this;
    }
    
    /**
     * Puts a {@code float} as a big-endian 32 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getFloat()
     */
    public Buffer putFloat(float value) {
        return putInt(Float.floatToRawIntBits(value));
    }
    
    /**
     * Puts a {@code double} as a big-endian 64 bit value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getDouble()
     */
    public Buffer putDouble(double value) {
        return putLong(Double.doubleToRawLongBits(value));
    }
    
    /**
     * Puts a {@code boolean} as an 8 bit value.<br>
     * Puts zero if value is {@code false} and one if value is {@code true}.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getBoolean()
     */
    public Buffer putBoolean(boolean value) {
        return putByte(value ? 1 : 0);
    }
    
    /**
     * Puts a {@code String} as bytes ending with a line break character {@code '\n'}.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getLine()
     */
    public Buffer putLine(String value) {
        requireBitAccess(false);
        requirePuttingCapacity(value.length() + 1);
        System.arraycopy(value.getBytes(), 0, data, pos, value.length());
        pos += value.length();
        data[pos++] = '\n';
        return this;
    }
    
    /**
     * Puts a {@code String} as bytes ending with a null character {@code '\0'}.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getString()
     */
    public Buffer putString(String value) {
        requireBitAccess(false);
        requirePuttingCapacity(value.length() + 1);
        System.arraycopy(value.getBytes(), 0, data, pos, value.length());
        pos += value.length();
        data[pos++] = 0;
        return this;
    }
    
    /**
     * TODO document
     */
    public Buffer putBuffer(Buffer buf) {
        return putBuffer(buf, 0, buf.getPosition());
    }
    
    /**
     * TODO document
     */
    public Buffer putBuffer(Buffer buf, int offset, int length) {
        if (buf == null) {
            throw new IllegalArgumentException("Buffer must not be null.");
        }
        return putBytes(buf.data, offset, length);
    }
    
    /**
     * A shorthand alternative to {@link #putBytes(byte[], int, int)}.<br>
     * Equivalent to calling: <pre>putBytes(array, 0, array.length)</pre>
     * Returns itself to allow method chaining.
     * 
     * @see #putBytes(byte[], int, int)
     * @see #getBytes(byte[], int, int)
     */
    public Buffer putBytes(byte[] array) {
        return putBytes(array, 0, array.length);
    }
    
    /**
     * Puts a {@code byte[]} as an array copy.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #putBytes(byte[])
     * @see #getBytes(byte[], int, int)
     */
    public Buffer putBytes(byte[] array, int offset, int length) {
        requireBitAccess(false);
        requirePuttingCapacity(length);
        checkByteArray(array, offset + length);
        System.arraycopy(array, offset, data, pos, length);
        pos += length;
        return this;
    }
    
    /**
     * A shorthand alternative to {@link #putBytesReversely(byte[], int, int)}.<br>
     * Equivalent to calling: <pre>putBytesReversely(array, 0, array.length)</pre>
     * Returns itself to allow method chaining.
     * 
     * @see #putBytesReversely(byte[], int, int)
     * @see #getBytesReversely(byte[], int, int)
     */
    public Buffer putBytesReversely(byte[] array) {
        return putBytesReversely(array, 0, array.length);
    }
    
    /**
     * Puts a {@code byte[]} as an array copy in a reversed byte order.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #putBytesReversely(byte[])
     * @see #getBytesReversely(byte[], int, int)
     */
    public Buffer putBytesReversely(byte[] array, int offset, int length) {
        requireBitAccess(false);
        requirePuttingCapacity(length);
        checkByteArray(array, offset + length);
        for (int i = offset + length - 1; i >= offset; i--) {
            data[pos++] = array[i];
        }
        return this;
    }
    
    public Buffer putAddedBytes(byte[] array, int offset, int length) {
        requireBitAccess(false);
        requirePuttingCapacity(length);
        checkByteArray(array, offset + length);
        for (int i = offset + length - 1; i >= offset; i--) {
            data[pos++] = (byte) (array[i] + 128);
        }
        return this;
    }
    
    // TODO: Ensure smart putting methods.
    
    /**
     * Puts an {@code int} as an signed smart (8 or 16 bit) value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getSmart()
     */
    public Buffer putSmart(int value) {
        if (value < 0x80) {
            return putByte(value + 0x40);
        } else {
            return putShort(value + 0xc000);
        }
    }
    
    /**
     * Puts an {@code int} as an unsigned smart (8 or 16 bit) value.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getUSmart()
     */
    public Buffer putUSmart(int value) {
        if (value < 0x80) {
            return putByte(value);
        } else {
            return putShort(value + 0x8000);
        }
    }
    
    /**
     * Gets a signed 8 bit value as a {@code byte}.
     * 
     * @see #putByte(int)
     */
    public byte getByte() {
        requireBitAccess(false);
        requireGettingCapacity(1);
        return data[pos++];
    }
    
    /**
     * Gets an unsigned 8 bit value as an {@code int}.<br>
     * 
     * @see #putByte(int)
     */
    public int getUByte() {
        requireBitAccess(false);
        requireGettingCapacity(1);
        return data[pos++] & 0xff;
    }
    
    /**
     * Gets a signed 8 bit value with negation data transformation as a
     * {@code byte}.
     * 
     * @see #putNegatedByte(int)
     */
    public byte getNegatedByte() {
        requireBitAccess(false);
        requireGettingCapacity(1);
        return (byte) -data[pos++];
    }
    
    /**
     * Gets an unsigned 8 bit value with negation data transformation as an
     * {@code int}.
     * 
     * @see #putNegatedByte(int)
     */
    public int getNegatedUByte() {
        requireBitAccess(false);
        requireGettingCapacity(1);
        return -data[pos++] & 0xff;
    }
    
    /**
     * Gets a signed 8 bit value with subtraction data transformation as a
     * {@code byte}.
     * 
     * @see #putSubtractedByte(int)
     */
    public byte getSubtractedByte() {
        requireBitAccess(false);
        requireGettingCapacity(1);
        return (byte) (128 - data[pos++]);
    }
    
    /**
     * Gets an unsigned 8 bit value with subtraction data transformation as an
     * {@code int}.
     * 
     * @see #putSubtractedByte(int)
     */
    public int getSubtractedUByte() {
        requireBitAccess(false);
        requireGettingCapacity(1);
        return 128 - data[pos++] & 0xff;
    }
    
    /**
     * Gets a signed 8 bit value with addition data transformation as a
     * {@code byte}.
     * 
     * @see #putAddedByte(int)
     */
    public byte getAddedByte() {
        requireBitAccess(false);
        requireGettingCapacity(1);
        return (byte) (data[pos++] - 128);
    }
    
    /**
     * Gets an unsigned 8 bit value with addition data transformation as an
     * {@code int}.
     * 
     * @see #putAddedByte(int)
     */
    public int getAddedUByte() {
        requireBitAccess(false);
        requireGettingCapacity(1);
        return data[pos++] - 128 & 0xff;
    }
    
    /**
     * Gets a signed big-endian 16 bit value as an {@code int}.
     * 
     * @see #putShort(int)
     */
    public int getShort() {
        requireBitAccess(false);
        requireGettingCapacity(2);
        pos += 2;
        int tmp = ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] & 0xff);
        if (tmp > 0x7fff) {
            tmp -= 0x10000;
        }
        return tmp;
    }
    
    /**
     * Gets an unsigned big-endian 16 bit value as an {@code int}.
     * 
     * @see #putShort(int)
     */
    public int getUShort() {
        requireBitAccess(false);
        requireGettingCapacity(2);
        pos += 2;
        return ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] & 0xff);
    }
    
    /**
     * Gets a signed little-endian 16 bit value as an {@code int}.
     * 
     * @see #putLeShort(int)
     */
    public int getLeShort() {
        requireBitAccess(false);
        requireGettingCapacity(2);
        pos += 2;
        int tmp = ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] & 0xff);
        if (tmp > 0x7fff) {
            tmp -= 0x10000;
        }
        return tmp;
    }
    
    /**
     * Gets an unsigned little-endian 16 bit value as an {@code int}.
     * 
     * @see #putLeShort(int)
     */
    public int getLeUShort() {
        requireBitAccess(false);
        requireGettingCapacity(2);
        pos += 2;
        return ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] & 0xff);
    }
    
    /**
     * Gets a signed big-endian 16 bit value with addition data transformation as an {@code int}.
     * 
     * @see #putAddedShort(int)
     */
    public int getAddedShort() {
        requireBitAccess(false);
        requireGettingCapacity(2);
        pos += 2;
        int tmp = ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] - 128 & 0xff);
        if (tmp > 0x7fff) {
            tmp -= 0x10000;
        }
        return tmp;
    }
    
    /**
     * Gets an unsigned big-endian 16 bit value with addition data transformation as an {@code int}.
     * 
     * @see #putAddedShort(int)
     */
    public int getAddedUShort() {
        requireBitAccess(false);
        requireGettingCapacity(2);
        pos += 2;
        return ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] - 128 & 0xff);
    }
    
    /**
     * Gets a signed little-endian 16 bit value with addition data transformation as an {@code int}.
     * 
     * @see #putAddedLeShort(int)
     */
    public int getAddedLeShort() {
        requireBitAccess(false);
        requireGettingCapacity(2);
        pos += 2;
        int tmp = ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] - 128 & 0xff);
        if (tmp > 0x7fff) {
            tmp -= 0x10000;
        }
        return tmp;
    }
    
    /**
     * Gets an unsigned little-endian 16 bit value with addition data transformation as an {@code int}.
     * 
     * @see #putAddedLeShort(int)
     */
    public int getAddedLeUShort() {
        requireBitAccess(false);
        requireGettingCapacity(2);
        pos += 2;
        return ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] - 128 & 0xff);
    }
    
    /**
     * Gets a signed big-endian 24 bit value as an {@code int}.
     * 
     * @see #putMedium(int)
     */
    public int getMedium() {
        requireBitAccess(false);
        requireGettingCapacity(3);
        pos += 3;
        int tmp = ((data[pos - 3] & 0xff) << 16)
                + ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] & 0xff);
        if (tmp > 0x7fffff) {
            tmp -= 0x1000000;
        }
        return tmp;
    }
    
    /**
     * Gets an unsigned big-endian 24 bit value as an {@code int}.
     * 
     * @see #putMedium(int)
     */
    public int getUMedium() {
        requireBitAccess(false);
        requireGettingCapacity(3);
        pos += 3;
        return ((data[pos - 3] & 0xff) << 16) + ((data[pos - 2] & 0xff) << 8)
                + (data[pos - 1] & 0xff);
    }
    
    /**
     * Gets a signed little-endian 24 bit value as an {@code int}.
     * 
     * @see #putLeMedium(int)
     */
    public int getLeMedium() {
        requireBitAccess(false);
        requireGettingCapacity(3);
        pos += 3;
        int tmp = ((data[pos - 1] & 0xff) << 16)
                + ((data[pos - 2] & 0xff) << 8) + (data[pos - 3] & 0xff);
        if (tmp > 0x7fffff) {
            tmp -= 0x1000000;
        }
        return tmp;
    }
    
    /**
     * Gets an unsigned little-endian 24 bit value as an {@code int}.
     * 
     * @see #putLEMedium()
     */
    public int getLeUMedium() {
        requireBitAccess(false);
        requireGettingCapacity(3);
        pos += 3;
        return ((data[pos - 1] & 0xff) << 16) + ((data[pos - 2] & 0xff) << 8)
                + (data[pos - 3] & 0xff);
    }
    
    /**
     * Gets a signed big-endian 32 bit value as an {@code int}.
     * 
     * @see #putInt(int)
     */
    public int getInt() {
        requireBitAccess(false);
        requireGettingCapacity(4);
        pos += 4;
        return ((data[pos - 4] & 0xff) << 24) + ((data[pos - 3] & 0xff) << 16)
                + ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] & 0xff);
    }
    
    /**
     * Gets a signed little-endian 32 bit value as an {@code int}.
     * 
     * @see #putLeInt(int)
     */
    public int getLeInt() {
        requireBitAccess(false);
        requireGettingCapacity(4);
        pos += 4;
        return ((data[pos - 1] & 0xff) << 24) + ((data[pos - 2] & 0xff) << 16)
                + ((data[pos - 3] & 0xff) << 8) + (data[pos - 4] & 0xff);
    }
    
    /**
     * Gets a signed middle-endian 32 bit value as an {@code int}.
     * 
     * @see #putMeInt(int)
     */
    public int getMeInt() {
        requireBitAccess(false);
        requireGettingCapacity(4);
        pos += 4;
        return ((data[pos - 3] & 0xff) << 24) + ((data[pos - 4] & 0xff) << 16)
                + ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] & 0xff);
    }
    
    /**
     * Gets a signed reversed middle-endian 32 bit value as an {@code int}.
     * 
     * @see #putRmeInt(int)
     */
    public int getRmeInt() {
        requireBitAccess(false);
        requireGettingCapacity(4);
        pos += 4;
        return ((data[pos - 2] & 0xff) << 24) + ((data[pos - 1] & 0xff) << 16)
                + ((data[pos - 4] & 0xff) << 8) + (data[pos - 3] & 0xff);
    }
    
    /**
     * Gets a signed big-endian 64 bit value as a {@code long}.
     * 
     * @see #putLong(long)
     */
    public long getLong() {
        requireBitAccess(false);
        requireGettingCapacity(8);
        long l = getInt() & 0xffffffffL;
        long l1 = getInt() & 0xffffffffL;
        return (l << 32) + l1;
    }
    
    /**
     * Gets a big-endian 32 bit value as a {@code float}.
     * 
     * @see #putFloat(float)
     */
    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }
    
    /**
     * Gets a big-endian 64 bit value as a {@code double}.
     * 
     * @see #putDouble(double)
     */
    public double getDouble() {
        return Double.longBitsToDouble(getLong());
    }
    
    /**
     * Gets an 8 bit value as a {@code boolean}.<br>
     * Returns {@code false} if the byte is zero and {@code true} otherwise.
     * 
     * @see #putBoolean(boolean)
     */
    public boolean getBoolean() {
        return getUByte() != 0;
    }
    
    /**
     * Gets a {@code String} created from bytes ending with a line break
     * character {@code '\n'}.
     * 
     * @see #putLine(String)
     */
    public String getLine() {
        requireBitAccess(false);
        int begin = pos;
        do {
            if (pos >= data.length) {
                throw new ArrayIndexOutOfBoundsException("Buffer overflow.");
            }
        } while (data[pos++] != '\n');
        return new String(data, begin, pos - begin - 1);
    }
    
    /**
     * Gets a {@code String} created from bytes ending with a null character
     * {@code '\0'}.
     * 
     * @see #putString(String)
     */
    public String getString() {
        requireBitAccess(false);
        int begin = pos;
        do {
            if (pos >= data.length) {
                throw new ArrayIndexOutOfBoundsException("Buffer overflow.");
            }
        } while (data[pos++] != 0);
        return new String(data, begin, pos - begin - 1);
    }
    
    /**
     * TODO document
     */
    public Buffer getBuffer(Buffer buf) {
        if (buf == null) {
            throw new IllegalArgumentException("Buffer must not be null.");
        }
        return getBuffer(buf, buf.getPosition(), buf.getRemaining());
    }
    
    /**
     * TODO document
     */
    public Buffer getBuffer(Buffer buf, int offset, int length) {
        if (buf == null) {
            return new Buffer(getBytes(null, offset, length));
        } else {
            byte[] data = getBytes(buf.data, offset, length);
            return data == buf.data ? buf : new Buffer(data);
        }
    }
    
    /**
     * TODO document
     */
    public byte[] getBytes(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null.");
        }
        return getBytes(array, 0, array.length);
    }
    
    /**
     * Gets a {@code byte[]} as an array copy.<br>
     * If destination array is {@code null}, it is automatically created with a
     * length that is able to store the required bytes.
     * 
     * @see #putBytes(byte[])
     * @see #putBytes(byte[], int, int)
     * @return The array the bytes were stored to.
     */
    public byte[] getBytes(byte[] array, int offset, int length) {
        requireBitAccess(false);
        requireGettingCapacity(length);
        if (array == null) {
            array = new byte[offset + length];
        } else {
            checkByteArray(array, offset + length);
        }
        System.arraycopy(array, offset, data, pos, length);
        pos += length;
        return array;
    }
    
    /**
     * Gets a {@code byte[]} as an array copy in a reversed byte order. If
     * destination array is {@code null}, it is automatically created with a
     * length that is able to store the required bytes.
     * 
     * @see #putBytesReversely(byte[])
     * @see #putBytesReversely(byte[], int, int)
     * @return The array the bytes were stored to.
     */
    public byte[] getBytesReversely(byte[] array, int offset, int length) {
        requireBitAccess(false);
        requireGettingCapacity(length);
        if (array == null) {
            array = new byte[offset + length];
        } else {
            checkByteArray(array, offset + length);
        }
        for (int i = offset + length - 1; i >= offset; i--) {
            array[i] = data[pos++];
        }
        return array;
    }
    
    /**
     * Gets a signed smart (8 or 16 bit) value as an {@code int}.
     * 
     * @see #putSmart(int)
     */
    public int getSmart() {
        requireBitAccess(false);
        int tmp = data[pos] & 0xff;
        if (tmp < 0x80) {
            return getUByte() - 0x40;
        } else {
            return getUShort() - 0xc000;
        }
    }
    
    /**
     * Gets an unsigned smart (8 or 16 bit) value as an {@code int}.
     * 
     * @see #putUSmart(int)
     */
    public int getUSmart() {
        requireBitAccess(false);
        int tmp = data[pos] & 0xff;
        if (tmp < 0x80) {
            return getUByte();
        } else {
            return getUShort() - 0x8000;
        }
    }
    
    public int getSmart32() {
        requireBitAccess(false);
        if (data[pos] >= 0) {
            return getUShort() & 0x7fff;
        }
        return getInt() & 0x7fffffff;
    }
    
    /**
     * Begins a bit access block.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #endBitAccess()
     * @see #isBitAccess()
     */
    public Buffer beginBitAccess() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is already enabled.");
        }
        pos <<= 3;
        bitAccess = true;
        return this;
    }
    
    /**
     * Ends a bit access block.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #beginBitAccess()
     * @see #isBitAccess()
     */
    public Buffer endBitAccess() {
        if (!bitAccess) {
            throw new IllegalStateException("Bit access is already disabled.");
        }
        pos = (pos + 7) >> 3;
        bitAccess = false;
        return this;
    }
    
    /**
     * Returns {@code true} if bit acces is enabled, {@code false} otherwise.
     * 
     * @see #beginBitAccess()
     * @see #endBitAccess()
     */
    public boolean isBitAccess() {
        return bitAccess;
    }
    
    /**
     * Puts an {@code int} as an {@code n} bit value.<br>
     * Can only be used in a bit access block.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getBits(int)
     */
    public Buffer putBits(int n, int value) {
        if (n < 0 || n > 32) {
            throw new IllegalArgumentException("Amount out of bounds: " + n);
        }
        requireBitAccess(true);
        requireBitPuttingCapacity(n);
        int bytepos = pos >> 3;
        int bitoff = 8 - (pos & 7);
        pos += n;
        for (; n > bitoff; bitoff = 8) {
            data[bytepos] &= ~BIT_MASK[bitoff];
            data[bytepos++] |= (value >> (n - bitoff)) & BIT_MASK[bitoff];
            n -= bitoff;
        }
        if (n == bitoff) {
            data[bytepos] &= ~BIT_MASK[bitoff];
            data[bytepos] |= value & BIT_MASK[bitoff];
        } else {
            data[bytepos] &= ~(BIT_MASK[n] << (bitoff - n));
            data[bytepos] |= (value & BIT_MASK[n]) << (bitoff - n);
        }
        return this;
    }
    
    /**
     * Puts a {@code boolean} as a single bit value.<br>
     * Can only be used in a bit access block.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getBit()
     */
    public Buffer putBit(boolean value) {
        return putBits(1, value ? 1 : 0);
    }
    
    /**
     * Gets an {@code n} bit value as an {@code int}.<br>
     * Can only be used in a bit access block.
     * 
     * @see #putBits(int, int)
     */
    public int getBits(int n) {
        if (n < 0 || n > 32) {
            throw new IllegalArgumentException("Amount out of bounds: " + n);
        }
        requireBitAccess(true);
        requireBitGettingCapacity(n);
        int bytepos = pos >> 3;
        int bitoff = 8 - (pos & 7);
        int value = 0;
        pos += n;
        for (; n > bitoff; bitoff = 8) {
            value += (data[bytepos++] & BIT_MASK[bitoff]) << n - bitoff;
            n -= bitoff;
        }
        if (n == bitoff) {
            value += data[bytepos] & BIT_MASK[bitoff];
        } else {
            value += data[bytepos] >> bitoff - n & BIT_MASK[n];
        }
        return value;
    }
    
    /**
     * Gets a single bit value as a {@code boolean}.<br>
     * Can only be used in a bit access block.
     * 
     * @see #putBit(boolean)
     */
    public boolean getBit() {
        return getBits(1) != 0;
    }
}
