package com.ccreanga.gameproxy.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

@Data
public abstract class ClientMessage {

    public static final short CLIENT_LOGIN = 1;
    public static final short CLIENT_LOGOUT = 2;
    public static final short CLIENT_SEND_DATA = 3;

    protected int messageType;

    abstract void writeExternal(OutputStream out) throws IOException;

    abstract void readExternal(InputStream in) throws IOException;
}