package com.test.io.outputstream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class BufferedOutputStreamTest {
    private BufferedOutputStream bufferedOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] content;

    @Before
    public void before() throws Exception {
        content = "Hello !".getBytes();
        byteArrayOutputStream = new ByteArrayOutputStream();
        bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
    }

    @After
    public void after() throws Exception {
        bufferedOutputStream.close();
        byteArrayOutputStream.close();
    }

    @Test
    public void writeTest() throws Exception {
        bufferedOutputStream.write(72);
        bufferedOutputStream.close();
        assertEquals(72, byteArrayOutputStream.toByteArray()[0]);
    }

    @Test
    public void writeWithBufferTest() throws Exception {
        bufferedOutputStream.write(content);
        bufferedOutputStream.close();
        int i = 0;
        for (byte b : byteArrayOutputStream.toByteArray()) {
            assertEquals(content[i], b);
            i++;
        }
    }

    @Test
    public void writeWithBufferAndParametersTest() throws Exception {
        bufferedOutputStream.write(content, 2, 2);
        bufferedOutputStream.close();

        assertEquals(108, byteArrayOutputStream.toByteArray()[0]);
        assertEquals(108, byteArrayOutputStream.toByteArray()[1]);

    }

    @Test(expected = NullPointerException.class)
    public void NullPointerExceptionWriteTest() throws Exception {
        bufferedOutputStream.write(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readWithBufferExceptionTest() throws IOException {
        byte[] array = new byte[0];
        bufferedOutputStream.write(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readWithBufferAndParameterOffExceptionTest() throws IOException {
        int off = 6;
        byte[] array = new byte[5];
        bufferedOutputStream.write(array, off, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readWithBufferAndParameterLenExceptionTest() throws IOException {
        int len = 6;
        byte[] array = new byte[5];
        bufferedOutputStream.write(array, 0, len);
    }

    @Test
    public void flush() throws Exception {
        bufferedOutputStream.write(content, 2, 2);
        assertEquals(2, bufferedOutputStream.getIndex());
        bufferedOutputStream.flush();
        assertEquals(0, bufferedOutputStream.getIndex());
    }

    @Test(expected = IOException.class)
    public void close() throws Exception {
        bufferedOutputStream.write(72);
        bufferedOutputStream.close();
        bufferedOutputStream.write(72);
    }

}