package com.test.io.inputstream;

import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStream extends InputStream {
    private static final int INITIAL_CAPACITY = 5;

    private InputStream inputStream;
    private byte[] buffer;
    private int index;
    private int count;

    public BufferedInputStream(InputStream inputStream) {
        this(inputStream, INITIAL_CAPACITY);
    }

    public BufferedInputStream(InputStream inputStream, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Incorrect buffer size: " + size + ", should be more than 0");
        }
        this.inputStream = inputStream;
        buffer = new byte[size];
    }

    @Override
    public int read() throws IOException {
        checkIfBufferIsNull();
        fillBuffer();
        return count == -1 ? -1 : buffer[index++];
    }

    @Override
    public int read(byte[] array) throws IOException {
        return read(array, 0, array.length);
    }

    @Override
    public int read(byte[] array, int off, int len) throws IOException {
        checkIfBufferIsNull();
        validateParams(array, off, len);

        if (len == 0) {
            return 0;
        }

        if (len > buffer.length) {
            System.arraycopy(new byte[array.length], 0, array, 0, len);
            return inputStream.read(array, off, len);
        }

        if (fillBuffer() == -1) {
            return -1;
        }

        int availableBytes = count - index;
        int copiedBytes;

        if (len <= availableBytes) {
            System.arraycopy(buffer, index, array, off, len);
            index += len;
            copiedBytes = len;
        } else {
            System.arraycopy(buffer, index, array, off, availableBytes);
            index += availableBytes;
            copiedBytes = availableBytes;

            int extraBytes = len - copiedBytes;

            if (fillBuffer() == -1) {
                System.arraycopy(new byte[array.length], off + copiedBytes, array, off + copiedBytes, extraBytes);
            } else {
                if (count < extraBytes) {
                    System.arraycopy(buffer, index, array, off + copiedBytes, count);
                    copiedBytes += count;
                    index += count;
                    System.arraycopy(new byte[array.length], off + copiedBytes, array, off + copiedBytes, off + len);
                } else {
                    System.arraycopy(buffer, index, array, off + copiedBytes, extraBytes);
                    copiedBytes += extraBytes;
                    index += extraBytes;
                }
            }
        }
        return copiedBytes;
    }

    @Override
    public void close() throws IOException {
        index = 0;
        count = 0;
        buffer = null;
        inputStream.close();
    }

    private int fillBuffer() throws IOException {
        if (index == count) {
            count = inputStream.read(buffer);
            index = 0;
        }
        return count;
    }

    private void validateParams(byte[] array, int off, int len) throws IOException {

        if (array == null) {
            throw new IOException("userArray is null");
        }

        if (array.length <= 0) {
            throw new IllegalArgumentException("The length " + array.length + " is not valid");
        }

        if (off < 0 || off > array.length) {
            throw new IllegalArgumentException("The position should be between 0 and " + array.length);
        }
        if (len < 0 || len > array.length - off) {
            throw new IllegalArgumentException("The length should be between 0 and " + array.length);
        }
    }

    private void checkIfBufferIsNull() throws IOException {
        if (buffer == null) {
            throw new IOException("BufferedOutputStream is closed");
        }
    }

}