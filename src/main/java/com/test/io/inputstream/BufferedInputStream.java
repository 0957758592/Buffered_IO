package com.test.io.inputstream;

import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStream extends InputStream {
    private static final int INITIAL_CAPACITY = 5;

    private InputStream inputStream;
    private byte[] buffer = new byte[INITIAL_CAPACITY];
    private int index;
    private int count;

    public BufferedInputStream(InputStream inputStream) {
        this(inputStream, INITIAL_CAPACITY);
    }

    public BufferedInputStream(InputStream inputStream, int size) {
        this.inputStream = inputStream;
        if (size <= 0) {
            throw new IllegalArgumentException("your size is " + size + " but, should be increased");
        }
        buffer = new byte[size];
    }

    byte[] getBuffer() {
        return buffer;
    }

    @Override
    public int read() throws IOException {
        if (index == count) {
            count = inputStream.read(buffer);
            index = 0;
        }

        return count == -1 ? -1 : buffer[index++];
    }

    @Override
    public int read(byte[] array) throws IOException {

        if (array.length <= 0) {
            throw new IllegalArgumentException("The length " + array.length + " is not valid");
        }

        return read(array, 0, array.length);
    }

    @Override
    public int read(byte[] array, int off, int len) throws IOException {
        validateParams(array, off, len);

        if (fillCount() == -1) {
            return -1;
        }

        int availableBytes = count - index;
        int copiedBytes = 0;

        if (len <= availableBytes) {
            System.arraycopy(buffer, index, array, off, len);
            index += len;
            copiedBytes = len;
        } else {
            System.arraycopy(buffer, index, array, off, availableBytes);
            index += availableBytes;
            copiedBytes = availableBytes;

            int extraBytes = len - copiedBytes;

            if (fillCount() == -1) {
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

    private int fillCount() throws IOException {
        if (index == count) {
            count = inputStream.read(buffer);
            index = 0;
        }
        return count;
    }

    @Override
    public void close() throws IOException {
        index = 0;
        count = 0;
        inputStream.close();
    }

    private int getLength(int len, int unreadBytes) throws IOException {
        return len < unreadBytes ? len : unreadBytes;
    }

    private void validateParams(byte[] array, int off, int len) {
        if (array.length > buffer.length) {
            buffer = new byte[array.length];
        }
        if (off < 0 || off > buffer.length) {
            throw new IllegalArgumentException("The position should be between 0 and " + buffer.length);
        }
        if (array.length < len) {
            throw new IllegalArgumentException("The length should be no more than " + array.length);
        }
    }

//    private void cleanArray(byte[] array) {
//        System.arraycopy(new byte[array.length], 0, array, 0, array.length);
//    }

}

