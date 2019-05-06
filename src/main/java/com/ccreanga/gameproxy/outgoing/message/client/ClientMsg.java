package com.ccreanga.gameproxy.outgoing.message.client;


import java.io.IOException;
import java.io.OutputStream;

public interface ClientMsg {

    public static final short LOGIN = 1;
    public static final short LOGOUT = 2;
    public static final short HISTORICAL_DATA = 3;

    public void writeExternal(OutputStream out) throws IOException;

    public int getType();
}
