package com.ccreanga.gameproxy.outgoing.message.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class ServerMessage {

    public static final short LOGIN_RESULT = 1;
    public static final short DATA = 2;
    public static final short INFO = 3;

    protected int messageType;


    public abstract void writeExternal(OutputStream out) throws IOException;

    public abstract void readExternal(InputStream in) throws IOException;
}
