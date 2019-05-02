package com.ccreanga.gameproxy.util;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class IOUtil {

    /**
     * RFC 7320 - 6.6. Tear-down
     *
     * @param socket
     */
    public static void closeSocketPreventingReset(Socket socket) {
        try {
            socket.shutdownOutput();
            InputStream in = socket.getInputStream();
            int counter = 16000;//todo - try to find the best value
            while ((in.read() != -1) && (counter-- > 0)) ;
            socket.close();
        } catch (IOException e) {/**ignore**/}
    }

    public static void closeSilent(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {/**ignore**/}
    }


    public static void readFully(InputStream in, byte b[]) throws IOException {
        readFully(in, b, 0, b.length);
    }

    public static void readFully(InputStream in, byte b[], int off, int len) throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
            if (count < 0) {
                throw new EOFException();
            }
            n += count;
        }
    }

//    public static long readLong(InputStream in) throws IOException{
//        int a,b;
//        a = in.read();
//        b = in.read();
//        return (long)a << 32 | b & 0xFFFFFFFFL;
//    }
//    public static void writeLong(OutputStream out,long l) throws IOException{
//        out.write((int)(l >> 32));
//        out.write((int)l);
//    }

    public static long readLong(InputStream in) throws IOException {
        byte readBuffer[] = new byte[8];
        readFully(in, readBuffer);
        return (((long) readBuffer[0] << 56) +
            ((long) (readBuffer[1] & 255) << 48) +
            ((long) (readBuffer[2] & 255) << 40) +
            ((long) (readBuffer[3] & 255) << 32) +
            ((long) (readBuffer[4] & 255) << 24) +
            ((readBuffer[5] & 255) << 16) +
            ((readBuffer[6] & 255) << 8) +
            ((readBuffer[7] & 255) << 0));
    }

    public static void writeLong(OutputStream out, long v) throws IOException {
        byte writeBuffer[] = new byte[8];
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

    public static String getIp(Socket socket) {
        return socket.getRemoteSocketAddress().toString();
    }

    public static long copy(InputStream from, OutputStream to, int bufferSize) throws IOException {
        return copy(from, to, -1, -1, bufferSize, null);
    }


    public static long copy(InputStream from, OutputStream to) throws IOException {
        return copy(from, to, -1, -1);
    }

    public static long copy(InputStream from, OutputStream to, long inputOffset, long length) throws IOException {
        return copy(from, to, inputOffset, length, 8 * 1024, null);
    }

    public static long copy(InputStream from, OutputStream to, long inputOffset, long length, int bufferSize, MessageDigest md) throws IOException {

        byte[] buffer = new byte[bufferSize];

        if (inputOffset > 0) {
            long skipped = from.skip(inputOffset);
            if (skipped != inputOffset) {
                throw new EOFException("Bytes to skip: " + inputOffset + " actual: " + skipped);
            }

        }
        if (length == 0) {
            return 0;
        }
        final int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if (length > 0 && length < bufferLength) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && -1 != (read = from.read(buffer, 0, bytesToRead))) {
            to.write(buffer, 0, read);
            if (md != null)
                md.update(buffer, 0, read);
            totalRead += read;
            if (length > 0) {
                bytesToRead = (int) Math.min(length - totalRead, bufferLength);
            }
        }
        return totalRead;
    }

    public static LocalDateTime modifiedDateAsUTC(File file) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.of("UTC")).toLocalDateTime();
    }

    public static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }


}
