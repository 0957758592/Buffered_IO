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
        if (size <= 0) {
            throw new IllegalArgumentException("Incorrect buffer size: " + size);
        }

        this.outputStream = outputStream;
        buffer = new byte[size];
    }


    int getIndex() {
        return index;
    }

    @Override
    public void write(int value) throws IOException {
        bufferIsNull();
        if (index == buffer.length) {
            innerFlush();
        }
        buffer[index++] = (byte) value;
    }

    @Override
    public void write(byte[] array) throws IOException {
        write(array, 0, array.length);
    }

    @Override
    public void write(byte[] array, int off, int len) throws IOException {
        bufferIsNull();
        validateParameters(array, off, len);

        if (len > buffer.length) {
            outputStream.write(array, off, len);
            flush();
            return;
        }

        int availableBytes = buffer.length - index;

        if (availableBytes < len) {
            innerFlush();
            outputStream.write(array, off, len);
        } else {
            System.arraycopy(array, off, buffer, index, len);
            index += len;
        }
    }

    @Override
    public void flush() throws IOException {
        innerFlush();
        outputStream.flush();
    }

    private void innerFlush() throws IOException {
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

    private void validateParameters(byte[] array, int off, int len) throws IOException {
        if (array == null) {
            throw new IOException("buffer is null");
        }

        if (array.length <= 0) {
            throw new IllegalArgumentException("Incorrect buffer size: " + array.length);
        }

        if (off < 0 || off > array.length) {
            throw new IllegalArgumentException("The position should be between 0 and " + buffer.length);
        }
        if (len < 0 || len > array.length - off) {
            throw new IllegalArgumentException("The length should be between 0 and " + array.length);
        }
    }

    private void bufferIsNull() throws IOException {
        if (this.buffer == null) {
            throw new IOException("BufferedOutputStream is closed");
        }
    }

}