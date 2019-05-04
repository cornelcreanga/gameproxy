package com.ccreanga.gameproxy.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

@Data
public abstract class ClientMsg {

    public static final short LOGIN = 1;
    public static final short LOGOUT = 2;
    public static final short HISTORICAL_DATA = 3;

    protected int messageType;

    public ClientMsg(int messageType) {
        this.messageType = messageType;
    }

    public void writeExternal(OutputStream out) throws IOException {
        out.write(messageType);
    }

    abstract void readExternal(InputStream in) throws IOException;

    public int getType() {
        return messageType;
    }
}
