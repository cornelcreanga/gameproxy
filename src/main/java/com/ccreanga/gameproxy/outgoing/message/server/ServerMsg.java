package com.ccreanga.gameproxy.outgoing.message.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ServerMsg {

    public static final short DATA = 1;
    public static final short INFO = 2;
    public static final short DATA_END = 3;


    public void writeExternal(OutputStream out) throws IOException;

    public int getType();
}
