package com.ccreanga.protocol.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class FastDataOutputStream {

    private OutputStream out;

    public FastDataOutputStream(OutputStream out) {
        this.out = out;
    }

    public void writeUUID(UUID uuid) throws IOException {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        writeLong(msb);
        writeLong(lsb);
    }

//    public void writeLong(long v) throws IOException {
//        out.write((byte) (v >>> 56));
//        out.write((byte) (v >>> 48));
//        out.write((byte) (v >>> 40));
//        out.write((byte) (v >>> 32));
//        out.write((byte) (v >>> 24));
//        out.write((byte) (v >>> 16));
//        out.write((byte) (v >>> 8));
//        out.write((byte) (v));
//    }

    public void writeLong(long v) throws IOException {
        byte[] writeBuffer = new byte[8];
        writeBuffer[0] = (byte) (v >>> 56);
        writeBuffer[1] = (byte) (v >>> 48);
        writeBuffer[2] = (byte) (v >>> 40);
        writeBuffer[3] = (byte) (v >>> 32);
        writeBuffer[4] = (byte) (v >>> 24);
        writeBuffer[5] = (byte) (v >>> 16);
        writeBuffer[6] = (byte) (v >>> 8);
        writeBuffer[7] = (byte) (v >>> 0);
        out.write(writeBuffer, 0, 8);
    }


    public void writeLongs(long... v) throws IOException {
        for (long l : v) {
            writeLong(l);
        }
    }

    public void writeInt(int v) throws IOException {
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>>  8) & 0xFF);
        out.write((v) & 0xFF);
    }

    public void writeByte(int v) throws IOException {
        out.write(v);
    }

    /**
     * Write a byte array. first 4 bytes are  the array length (or zero for null arrays)
     * @return
     * @throws IOException
     */

    public void writeByteArray(byte[] b) throws IOException {
        if (b==null) {
            writeInt(0);
            return;
        }
        writeInt(b.length);
        write(b);
    }

    public void write(byte b[], int off, int len) throws IOException {
        out.write(b, off, len);
    }

    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }


    public void writeString(String s,String charset) throws IOException{
        if (s==null) {
            writeInt(0);
            return;
        }
        writeByteArray(s.getBytes(charset));
    }

}
