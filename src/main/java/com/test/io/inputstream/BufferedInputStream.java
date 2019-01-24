package com.test.io.inputstream;

import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStream extends InputStream {
    private static final int INITIAL_CAPACITY = 5;

    private InputStream inputStream;
    private byte[] buffer = new byte[INITIAL_CAPACITY];
    private int index;
    private int count;


    BufferedInputStream(InputStream inputStream) {
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
        cleanArray(array);

        if (index == count) {
            count = inputStream.read(buffer);
            index = 0;
        }

        int unreadBytes = count - index;

        if (count == -1) {
            return -1;
        } else {
            int length = getLength(len, unreadBytes);
            System.arraycopy(buffer, index, array, off, length);
            index += length;
            return length;
        }
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

    private void cleanArray(byte[] array) {
        System.arraycopy(new byte[array.length],0,array,0,array.length);
    }

}

