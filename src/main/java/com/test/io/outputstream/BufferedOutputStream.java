package com.test.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;

public class BufferedOutputStream extends OutputStream {
    private static final int INITIAL_CAPACITY = 5;
    private byte[] buffer;
    private OutputStream outputStream;
    private int index;


    public BufferedOutputStream(OutputStream outputStream) {
        this(outputStream, INITIAL_CAPACITY);
    }

    public BufferedOutputStream(OutputStream outputStream, int size) {
        if (outputStream == null) {
            throw new NullPointerException("OutputStream = null");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Incorrect buffer size: " + size);
        }

        this.outputStream = outputStream;
        this.buffer = new byte[size];
    }

    int getIndex() {
        return index;
    }

    @Override
    public void write(int value) throws IOException {
        bufferIsNull();
        if (index == buffer.length) {
            flush();
        }
        buffer[index++] = (byte) value;
    }

    @Override
    public void write(byte[] array) throws IOException {
        bufferIsNull();
        if (array.length <= 0) {
            throw new IllegalArgumentException("Incorrect buffer size: " + array.length);
        }
        if (array == null) {
            throw new NullPointerException("buffer is null");
        }
        write(array, 0, array.length);
    }

    @Override
    public void write(byte[] array, int off, int len) throws IOException {
        validateParameters(array, off, len);

        int availableBytes = buffer.length - index;

        if (availableBytes < len) {
            flush();
            outputStream.write(array, off, len);
        } else {
            System.arraycopy(array, off, buffer, index, len);
            index += len;
        }
    }

    @Override
    public void flush() throws IOException {
        if (index != 0) {
            outputStream.write(buffer, 0, index);
            index = 0;
        }
    }

    @Override
    public void close() throws IOException {
        flush();
        this.buffer = null;
        this.outputStream.close();
    }

    private void validateParameters(byte[] array, int off, int len) {
        if (array.length > buffer.length) {
            buffer = new byte[array.length];
        }
        if (off < 0 || off > array.length) {
            throw new IllegalArgumentException("The position should be between 0 and " + buffer.length);
        }
        if (len <= 0 || len > array.length) {
            throw new IllegalArgumentException("The length should be between 0 and " + array.length);
        }
    }

    private void bufferIsNull() throws IOException {
        if (this.buffer == null) {
            throw new IOException("BufferedOutputStream is closed");
        }
    }

}