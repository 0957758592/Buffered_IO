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
    private int count;
    private final String STRING = "Hello !";
    private InputStream inputStream;
    private BufferedInputStream bufferedInputStream;

    @Before
    public void before() throws IOException {

        File file = File.createTempFile("testFile", ".txt");

        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            br.write(STRING);
        }

        inputStream = new FileInputStream(file.getAbsolutePath());
        bufferedInputStream = new BufferedInputStream(inputStream);
    }

    @After
    public void after() throws IOException {
        bufferedInputStream.close();
        inputStream.close();
    }

    @Test
    public void readTest() throws Exception {
        for (int i = 0; i < STRING.length(); i++) {
            assertEquals(STRING.charAt(i), bufferedInputStream.read());
            count++;
        }
        assertEquals(count, STRING.length());
    }

    @Test
    public void readWithBufferTest() throws Exception {
        byte[] array = new byte[3];
        assertEquals(3, bufferedInputStream.read(array));

        assertEquals('H', array[0]);
        assertEquals('e', array[1]);
        assertEquals('l', array[2]);

        assertEquals(3, bufferedInputStream.read(array));

        assertEquals('l', array[0]);
        assertEquals('o', array[1]);
        assertEquals(' ', array[2]);

        assertEquals(1, bufferedInputStream.read(array));

        assertEquals('!', array[0]);
        assertEquals(0, array[1]);

    }

    @Test
    public void readCountBuffer() throws IOException {
        assertEquals(5, inputStream.read(new byte[5]));
    }

    @Test
    public void readWithBufferAndParametersTest() throws IOException {
        byte[] array = new byte[5];
        int off = 1;
        int len = 3;
        assertEquals(3, bufferedInputStream.read(array, off, len));

            assertEquals(0, array[0]);
            assertEquals('H', array[1]);
            assertEquals('e', array[2]);
            assertEquals('l', array[3]);
            assertEquals(0, array[4]);

    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionReadTest() throws IOException {
        bufferedInputStream.read(null);
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

    @Test(expected = IOException.class)
    public void close() throws Exception {
        bufferedInputStream.close();
        bufferedInputStream.read();
    }

}