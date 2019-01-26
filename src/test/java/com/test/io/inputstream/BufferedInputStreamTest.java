package com.test.io.inputstream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class BufferedInputStreamTest {
    private static final int INITIAL_CAPACITY = 5;
    private byte[] buffer = new byte[INITIAL_CAPACITY];
    private int count;
    private String string;
    private InputStream inputStream;
    private BufferedInputStream bufferedInputStream;
    private BufferedInputStream bufferedInputStreamWithSize;

    @Before
    public void before() throws IOException {
        string = "Hello !";
        File file = File.createTempFile("testFile", ".txt");

        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            br.write(string);
        }

        inputStream = new FileInputStream(file.getAbsolutePath());
        bufferedInputStream = new BufferedInputStream(inputStream);
    }

    @After
    public void after() throws IOException {
        bufferedInputStream.close();
    }

    @Test
    public void readTest() throws Exception {
        for (int i = 0; i < string.length(); i++) {
            assertEquals(string.charAt(i), bufferedInputStream.read());
            count++;
        }
        assertEquals(count, string.length());
    }

    @Test
    public void readWithBufferTest() throws Exception {
        byte[] array = new byte[3];
        assertEquals(3, bufferedInputStream.read(array));

        for (int i = 0; i < array.length; ) {
            assertEquals('H', array[i]);
            i++;
            assertEquals('e', array[i]);
            i++;
            assertEquals('l', array[i]);
            i++;
        }

        assertEquals(3, bufferedInputStream.read(array));

        for (int i = 0; i < array.length; ) {
            assertEquals('l', array[i]);
            i++;
            assertEquals('o', array[i]);
            i++;
            assertEquals(' ', array[i]);
            i++;
        }

        assertEquals(1, bufferedInputStream.read(array));

        for (int i = 0; i < array.length; ) {
            assertEquals('!', array[i]);
            i++;
            assertEquals(0, array[i]);
            i++;
            assertEquals(0, array[i]);
            i++;
        }
    }

    @Test
    public void readCountBuffer() throws IOException {
        assertEquals(5, inputStream.read(buffer));
    }

    @Test
    public void readWithBufferAndInputStreamSizeTest() throws IOException {
        int size = 300;
        bufferedInputStreamWithSize = new BufferedInputStream(inputStream, size);
        assertEquals(size, bufferedInputStreamWithSize.getBuffer().length);
    }

    @Test
    public void readWithBufferAndParametersTest() throws IOException {
        byte[] array = new byte[5];
        int off = 1;
        int len = 3;
        assertEquals(3, bufferedInputStream.read(array, off, len));

        for (int i = 0; i < array.length; ) {
            assertEquals(0, array[i]);
            i++;
            assertEquals('H', array[i]);
            i++;
            assertEquals('e', array[i]);
            i++;
            assertEquals('l', array[i]);
            i++;
            assertEquals(0, array[i]);
            i++;
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void readWithBufferAndInputStreamSizeExceptionTest() throws IOException {
        int size = 0;
        bufferedInputStreamWithSize = new BufferedInputStream(inputStream, size);
        assertEquals(size, bufferedInputStreamWithSize.getBuffer().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readWithBufferExceptionTest() throws IOException {
        byte[] array = new byte[0];
        bufferedInputStream.read(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readWithBufferAndParameterOffExceptionTest() throws IOException {
        int off = 6;
        byte[] array = new byte[5];
        bufferedInputStream.read(array, off, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readWithBufferAndParameterLenExceptionTest() throws IOException {
        int len = 6;
        byte[] array = new byte[5];
        bufferedInputStream.read(array, 0, len);
    }

    @Test
    public void close() throws Exception {
        assertEquals(0, count);
    }

}