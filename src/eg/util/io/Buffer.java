package eg.util.io;

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
     * Bit mask array used for bit based input and output.<br>
     * {@code BIT_MASK[i] = (1 << i) - 1}
     */
    private static final int[] BIT_MASK = {
        0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767,
        65535, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
        0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1
    };
    
    private byte[] data;
    private int pos;
    private boolean bitAccess;
    private boolean autoExpand;
    private int blockBegin;
    private int blockType;
    
    /**
     * Initializes a new {@link Buffer} by allocating space of the default
     * capacity (16). Automatic expanding is <b>enabled</b> by default.
     */
    public Buffer() {
        data = new byte[16];
        autoExpand = true;
    }
    
    /**
     * Initializes a new {@link Buffer} by allocating space of the required
     * capacity. Automatic expanding is <b>disabled</b> by default.
     */
    public Buffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be one or more.");
        }
        data = new byte[capacity];
    }
    
    /**
     * Initializes a new {@link Buffer} by wrapping the given {@code byte[]}.
     * Automatic expanding is <b>disabled</b> by default.
     */
    public Buffer(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null.");
        } else if (data.length == 0) {
            throw new IllegalArgumentException(
                    "Data capacity must not be zero.");
        }
        this.data = data;
    }
    
    /**
     * Gets the current position as the index of the byte.
     */
    public int getPosition() {
        if (bitAccess) {
            return (pos + 7) >> 3;
        } else {
            return pos;
        }
    }
    
    /**
     * Gets the current position as the index of the bit.
     */
    public int getBitPosition() {
        if (bitAccess) {
            return pos;
        } else {
            return pos << 3;
        }
    }
    
    /**
     * Sets the current position based on the index of the byte.<br>
     * Returns itself to allow method chaining.
     */
    public Buffer setPosition(int value) {
        if (bitAccess) {
            pos = value << 3;
        } else {
            pos = value;
        }
        return this;
    }
    
    /**
     * Sets the current position based on the index of the bit.<br>
     * Returns itself to allow method chaining.
     */
    public Buffer setBitPosition(int value) {
        if (bitAccess) {
            pos = value;
        } else {
            pos = (pos + 7) >> 3;
        }
        return this;
    }
    
    /**
     * Shifts the current position based on the amount of bytes.<br>
     * Returns itself to allow method chaining.
     */
    public Buffer shiftPosition(int n) {
        if (bitAccess) {
            pos += n << 3;
        } else {
            pos += n;
        }
        return this;
    }
    
    /**
     * Shifts the current position based on the amount of bits.<br>
     * Returns itself to allow method chaining.
     */
    public Buffer shiftBitPosition(int n) {
        if (bitAccess) {
            pos += n;
        } else {
            pos += (n + 7) >> 3;
        }
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
     * Gets the data as a modifiable {@code byte[]}. Replacing an element in the
     * returned array will also replace it in the buffer. Alternatively use
     * {@link #getUnmodifiableData()} or {@link #getShrunkUnmodifiableData()}.
     */
    public byte[] getData() {
        return data;
    }
    
    /**
     * Gets the data as an unmodifiable {@code byte[]}. Alternatively use
     * {@link #getData()} or {@link #getShrunkUnmodifiableData()}.
     */
    public byte[] getUnmodifiableData() {
        return data.clone();
    }
    
    /**
     * Gets the data as an unmodifiable {@code byte[]} which is shrunk to a size
     * based on the current position. Alternatively use {@link #getData()} or
     * {@link #getUnmodifiableData()}.
     */
    public byte[] getShrunkUnmodifiableData() {
        int size = getPosition();
        if (size == data.length) {
            return data.clone();
        } else {
            byte[] tmp = new byte[size];
            System.arraycopy(data, 0, tmp, 0, size);
            return tmp;
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
    
    /**
     * Checks if an {@code 8 * n} bit value can be put in the buffer.
     */
    private boolean hasPuttingCapacity(int n) {
        return isPuttingPositionAvailable(pos + n - 1);
    }
    
    /**
     * Checks if an {@code n} bit value can be put in the buffer.
     */
    private boolean hasBitPuttingCapacity(int n) {
        return isPuttingPositionAvailable((pos + n - 1) >> 3);
    }
    
    /**
     * Checks if the the required position is available in the buffer.<br>
     * Expands the buffer capacity when needed if automatic expanding is
     * enabled.
     */
    private boolean isPuttingPositionAvailable(int pos) {
        if (pos < data.length) {
            return true;
        } else if (!autoExpand) {
            return false;
        } else {
            int newcapacity = data.length;
            while (pos >= (newcapacity <<= 1))
                ;
            byte[] newdata = new byte[newcapacity];
            System.arraycopy(data, 0, newdata, 0, data.length);
            data = newdata;
            return true;
        }
    }
    
    private boolean hasGettingCapacity(int n) {
        return pos + n - 1 < data.length;
    }
    
    private boolean hasBitGettingCapacity(int n) {
        return (pos + n - 1) >> 3 < data.length;
    }
    
    /**
     * Begins a block with an unsigned 8 bit size variable header.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #endBlock()
     * @see #isBlock()
     */
    public Buffer beginUByteBlock() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (blockType == 0) {
            throw new IllegalStateException("No block exists.");
        }
        final int blockSize = pos - blockBegin;
        if (blockSize < 0) {
            throw new RuntimeException("Block size must not be negative: "
                    + blockSize);
        }
        if (blockType <= 2) {
            if (blockSize > 0xff) {
                throw new RuntimeException("Block size out of ubyte range: "
                        + blockSize);
            }
            data[blockBegin - 1] = (byte) (blockType == 1 ? blockSize
                    : -blockSize);
        } else {
            if (blockSize > 0xffff) {
                throw new RuntimeException("Block size out of ushort range: "
                        + blockSize);
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        data[pos++] = (byte) value;
        return this;
    }
    
    /**
     * Puts an {@code int} as an 8 bit value with an opposite sign.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getNegatedByte()
     * @see #getNegatedUByte()
     */
    public Buffer putNegatedByte(int value) {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        data[pos++] = (byte) -value;
        return this;
    }
    
    public Buffer putSubtractedByte(int value) {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        data[pos++] = (byte) (128 - value);
        return this;
    }
    
    public Buffer putAddedByte(int value) {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + 1));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + 1));
        }
        data[pos++] = (byte) value;
        data[pos++] = (byte) (value >> 8);
        return this;
    }
    
    public Buffer putAddedShort(int value) {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + 1));
        }
        data[pos++] = (byte) (value >> 8);
        data[pos++] = (byte) (value + 128);
        return this;
    }
    
    public Buffer putAddedLeShort(int value) {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + 1));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(3)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + 2));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(3)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + 2));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(4)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + 3));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(4)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 3));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(4)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 3));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(4)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 3));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(8)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 7));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(value.length() + 1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + value.length()));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasPuttingCapacity(value.length() + 1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + value.length()));
        }
        System.arraycopy(value.getBytes(), 0, data, pos, value.length());
        pos += value.length();
        data[pos++] = 0;
        return this;
    }
    
    /**
     * Puts a {@code byte[]} as an array copy.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getBytes(byte[], int, int)
     */
    public Buffer putBytes(byte[] src, int offset, int length) {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (src == null) {
            throw new NullPointerException("Source array must not be null.");
        }
        if (offset + length > src.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "Source array is not big enough: " + (offset + length));
        }
        if (!hasPuttingCapacity(length)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + length - 1));
        }
        System.arraycopy(src, offset, data, pos, length);
        pos += length;
        return this;
    }
    
    /**
     * Puts a {@code byte[]} as an array copy in a reversed byte order.<br>
     * Returns itself to allow method chaining.
     * 
     * @see #getBytesReversely(byte[], int, int)
     */
    public Buffer putBytesReversely(byte[] src, int offset, int length) {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (src == null) {
            throw new NullPointerException("Source array must not be null.");
        }
        if (offset + length > src.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "Source array is not big enough: " + (offset + length));
        }
        if (!hasPuttingCapacity(length)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + length - 1));
        }
        for (int i = offset + length - 1; i >= offset; i--) {
            data[pos++] = src[i];
        }
        return this;
    }
    
    public Buffer putAddedBytes(byte[] src, int offset, int length) {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (src == null) {
            throw new NullPointerException("Source array must not be null.");
        }
        if (offset + length > src.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "Source array is not big enough: " + (offset + length));
        }
        if (!hasPuttingCapacity(length)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + (pos + length - 1));
        }
        for (int i = offset + length - 1; i >= offset; i--) {
            data[pos++] = (byte) (src[i] + 128);
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        return data[pos++];
    }
    
    /**
     * Gets an unsigned 8 bit value as an {@code int}.<br>
     * 
     * @see #putByte(int)
     */
    public int getUByte() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        return data[pos++] & 0xff;
    }
    
    /**
     * Gets a signed 8 bit value with an opposite sign modifier as an
     * {@code int}.
     * 
     * @see #putNegatedByte(int)
     */
    public byte getNegatedByte() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        return (byte) -data[pos++];
    }
    
    /**
     * Gets an unsigned 8 bit value with an opposite sign modifier as an
     * {@code int}.
     * 
     * @see #putNegatedByte(int)
     */
    public int getNegatedUByte() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        return -data[pos++] & 0xff;
    }
    
    public byte getSubtractedByte() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        return (byte) (128 - data[pos++]);
    }
    
    public int getSubtractedUByte() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        return 128 - data[pos++] & 0xff;
    }
    
    public byte getAddedByte() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        return (byte) (data[pos++] - 128);
    }
    
    public int getAddedUByte() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(1)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + pos);
        }
        return data[pos++] - 128 & 0xff;
    }
    
    /**
     * Gets a signed big-endian 16 bit value as an {@code int}.
     * 
     * @see #putShort(int)
     */
    public int getShort() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 1));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 1));
        }
        pos += 2;
        return ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] & 0xff);
    }
    
    /**
     * Gets a signed little-endian 16 bit value as an {@code int}.
     * 
     * @see #putLeShort(int)
     */
    public int getLeShort() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 1));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 1));
        }
        pos += 2;
        return ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] & 0xff);
    }
    
    public int getAddedShort() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 1));
        }
        pos += 2;
        int tmp = ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] - 128 & 0xff);
        if (tmp > 0x7fff) {
            tmp -= 0x10000;
        }
        return tmp;
    }
    
    public int getAddedUShort() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 1));
        }
        pos += 2;
        return ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] - 128 & 0xff);
    }
    
    public int getAddedLeShort() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 1));
        }
        pos += 2;
        int tmp = ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] - 128 & 0xff);
        if (tmp > 0x7fff) {
            tmp -= 0x10000;
        }
        return tmp;
    }
    
    public int getAddedLeUShort() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(2)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 1));
        }
        pos += 2;
        return ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] - 128 & 0xff);
    }
    
    /**
     * Gets a signed big-endian 24 bit value as an {@code int}.
     * 
     * @see #putMedium(int)
     */
    public int getMedium() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(3)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 2));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(3)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 2));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(3)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 2));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(3)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 2));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(4)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 3));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(4)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 3));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(4)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 3));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(4)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 3));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (!hasGettingCapacity(8)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + 7));
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        int begin = pos;
        do {
            if (pos >= data.length) {
                throw new ArrayIndexOutOfBoundsException("Buffer overflow.");
            }
        } while (data[pos++] != 0);
        return new String(data, begin, pos - begin - 1);
    }
    
    /**
     * Gets a {@code byte[]} as an array copy.<br>
     * If destination array is {@code null}, it is automatically created with a
     * length that is able to store the required bytes.
     * 
     * @see #putBytes(byte[], int, int)
     * @return The array the bytes where stored to.
     */
    public byte[] getBytes(byte[] dst, int offset, int length) {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (dst == null) {
            dst = new byte[offset + length];
        }
        if (offset + length > dst.length) {
            throw new ArrayIndexOutOfBoundsException("Destination array is not big enough: " + (offset + length));
        }
        if (!hasGettingCapacity(length)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + length - 1));
        }
        System.arraycopy(dst, offset, data, pos, length);
        pos += length;
        return dst;
    }
    
    /**
     * Gets a {@code byte[]} as an array copy in a reversed byte order. If
     * destination array is {@code null}, it is automatically created with a
     * length that is able to store the required bytes.
     * 
     * @see #putBytesReversely(byte[], int, int)
     * @return The array the bytes where stored to.
     */
    public byte[] getBytesReversely(byte[] dst, int offset, int length) {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        if (dst == null) {
            dst = new byte[offset + length];
        }
        if (offset + length > dst.length) {
            throw new ArrayIndexOutOfBoundsException("Destination array is not big enough: " + (offset + length));
        }
        if (!hasGettingCapacity(length)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: " + (pos + length - 1));
        }
        for (int i = offset + length - 1; i >= offset; i--) {
            dst[i] = data[pos++];
        }
        return dst;
    }
    
    /**
     * Gets a signed smart (8 or 16 bit) value as an {@code int}.
     * 
     * @see #putSmart(int)
     */
    public int getSmart() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        final int tmp = data[pos] & 0xff;
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
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
        final int tmp = data[pos] & 0xff;
        if (tmp < 0x80) {
            return getUByte();
        } else {
            return getUShort() - 0x8000;
        }
    }
    
    public int getSmart32() {
        if (bitAccess) {
            throw new IllegalStateException("Bit access is enabled.");
        }
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
            throw new IllegalArgumentException("Amount out of range: " + n);
        }
        if (!bitAccess) {
            throw new IllegalStateException("Bit access is disabled.");
        }
        if (!hasBitPuttingCapacity(n)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + ((pos + n - 1) >> 3));
        }
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
            throw new IllegalArgumentException("Amount out of range: " + n);
        }
        if (!bitAccess) {
            throw new IllegalStateException("Bit access is disabled.");
        }
        if (!hasBitGettingCapacity(n)) {
            throw new ArrayIndexOutOfBoundsException("Buffer overflow: "
                    + ((pos + n - 1) >> 3));
        }
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
