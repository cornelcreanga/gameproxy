package com.ccreanga.gameproxy.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FastDataInputStream{

    private InputStream in;

    public FastDataInputStream(InputStream in) {
        this.in = in;
    }

    public void readFully(byte b[]) throws IOException {
        readFully(b,0,b.length);
    }

    public byte[] readByteArray() throws IOException {
        int a = readInt();
        if (a==0)
            return null;
        byte[] b = new byte[a];
        readFully(b,0,b.length);
        return b;
    }

    public String readString(String charset) throws IOException{
        String s = null;
        byte[] b = readByteArray();
        if (b!=null)
            s = new String(b,charset);
        return s;
    }

    public String readString(String charset,int maxLength) throws IOException{
        String s = null;
        byte[] b = readByteArray();
        if (b!=null)
            s = new String(b,charset);
        return s;
    }


    public void readFully(byte b[], int off, int len) throws IOException {
        if (len < 0)
            throw new IndexOutOfBoundsException();
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
            if (count < 0)
                throw new EOFException();
            n += count;
        }
    }

    public long readLong() throws IOException {
        byte readBuffer[] = new byte[8];
        readFully(readBuffer, 0, 8);
        return (((long)readBuffer[0] << 56) +
            ((long)(readBuffer[1] & 255) << 48) +
            ((long)(readBuffer[2] & 255) << 40) +
            ((long)(readBuffer[3] & 255) << 32) +
            ((long)(readBuffer[4] & 255) << 24) +
            ((readBuffer[5] & 255) << 16) +
            ((readBuffer[6] & 255) <<  8) +
            ((readBuffer[7] & 255) <<  0));
    }

    public int readInt() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public UUID readUUID() throws IOException {
        long msb, lsb;
        msb = readLong();
        lsb = readLong();
        return new UUID(msb, lsb);
    }

    public final byte readByte() throws IOException {
        int ch = in.read();
        if (ch < 0)
            throw new EOFException();
        return (byte)(ch);
    }

}
