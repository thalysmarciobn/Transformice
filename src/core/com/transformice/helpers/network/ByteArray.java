package com.transformice.helpers.network;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class ByteArray {
    protected byte[] buf;
    protected int count;

    public ByteArray() {
        this(32);
    }

    public ByteArray(int size) {
        this.buf = new byte[size];
    }

    public ByteArray(byte[] buf) {
        this.buf = buf;
        this.count = buf.length;
    }

    public synchronized ByteArray write(int b) {
        this.ensureCapacity(this.count++);
        this.buf[this.count - 1] = (byte) b;
        return this;
    }

    public synchronized ByteArray write(byte b[]) {
        this.write(b, 0, b.length);
        return this;
    }

    public synchronized ByteArray write(byte b[], int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }

        this.ensureCapacity(this.count + len);
        System.arraycopy(b, off, this.buf, this.count, len);
        this.count += len;
        return this;
    }

    public ByteArray writeBoolean(boolean value) {
        this.write(value ? 1 : 0);
        return this;
    }

    public ByteArray writeByte(int value) {
        this.write(value);
        return this;
    }

    public ByteArray writeShort(int value) {
        this.write((value >>> 8) & 0xFF);
        this.write(value & 0xFF);
        return this;
    }

    public ByteArray writeInt(int value) {
        this.write((value >>> 24) & 0xFF);
        this.write((value >>> 16) & 0xFF);
        this.write((value >>> 8) & 0xFF);
        this.write(value & 0xFF);
        return this;
    }

    public ByteArray writeBytes(String s) {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            this.write((byte) s.charAt(i));
        }

        return this;
    }

    public ByteArray writeBytes(byte[] b) {
        return this.write(b);
    }

    public ByteArray writeUTF(String s) {
        int strlen = s.length();
        int utflen = 0;
        for (int i = 0; i < strlen; i++) {
            int c = s.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                utflen++;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }

        this.writeShort(utflen);
        int bytesCount = 0;
        byte[] bytes = new byte[utflen];

        for (int i = 0; i < strlen; i++) {
            int c = s.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                bytes[bytesCount++] = (byte) c;
            } else if (c > 0x07FF) {
                bytes[bytesCount++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytes[bytesCount++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                bytes[bytesCount++] = (byte) (0x80 | (c & 0x3F));
            } else {
                bytes[bytesCount++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                bytes[bytesCount++] = (byte) (0x80 | (c & 0x3F));
            }
        }

        this.write(bytes, 0, utflen);
        return this;
    }

    public synchronized int read() {
        int value = this.count > 0 ? (this.buf[0] & 0xff) : -1;
        this.buf = ArrayUtils.subarray(this.buf, 1, this.buf.length);
        this.count--;
        if (this.count < 0) this.count = 0;
        return value;
    }

    public synchronized byte[] read(byte[] b) {
        return this.read(b, 0, b.length);
    }

    public synchronized byte[] read(byte[] b, int off, int len) {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }

        System.arraycopy(this.buf, 0, b, off, len);
        this.count -= len;
        this.buf = ArrayUtils.subarray(this.buf, len, this.buf.length);
        return b;
    }

    public boolean readBoolean() {
        return (this.read() != 0);
    }

    public byte readByte() {
        return (byte) this.read();
    }

    public int readUnsignedByte() {
        return this.read();
    }

    public short readShort() {
        return (short) ((this.read() << 8) | this.read());
    }

    public int readUnsignedShort() {
        return (this.read() << 8) | this.read();
    }

    public int readInt() {
        return (this.read() << 24) | (this.read() << 16) | (this.read() << 8) | this.read();
    }

    public String readUTF() {
        int size = this.readUnsignedShort();
        byte[] bytearr = new byte[size * 2];
        char[] chararr = new char[size * 2];
        this.read(bytearr, 0, size);
        int chararr_count = 0;

        for (int i = 0; i < size; ) {
            int c = (int) bytearr[i] & 0xff;
            int x = c >> 4;
            if (x == 12 || x == 13) {
                i += 2;
                chararr[chararr_count++] = (char) (((c & 0x1F) << 6) | ((int) bytearr[i - 1] & 0x3F));
            } else if (x == 14) {
                i += 3;
                chararr[chararr_count++] = (char) (((c & 0x0F) << 12) | (((int) bytearr[i - 2] & 0x3F) << 6) | (((int) bytearr[i - 1] & 0x3F)));
            } else {
                i++;
                chararr[chararr_count++] = (char) c;
            }
        }

        return new String(chararr, 0, chararr_count);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity - this.buf.length >= 0) {
            this.grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int newCapacity = (this.buf.length << 1) - minCapacity < 0 ? minCapacity : this.buf.length << 1;
        this.buf = Arrays.copyOf(this.buf, newCapacity < 0 ? Integer.MAX_VALUE : newCapacity);
    }

    public void skipBytes(int n) {
        this.count -= n;
        this.buf = ArrayUtils.subarray(this.buf, n, this.buf.length);
    }

    public synchronized byte[] toByteArray() {
        return Arrays.copyOf(this.buf, this.count);
    }

    public synchronized int size() {
        return this.count;
    }

    public boolean bytesAvailable() {
        return this.count > 0;
    }
}