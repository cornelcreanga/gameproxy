package com.ccreanga.gameproxy.outgoing.message.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

@Data
public class LoginResultMessage extends ServerMessage {

    public static final short AUTHORIZED = 10;
    public static final short ALREADY_AUTHENTICATED = 20;
    public static final short UNAUTHORIZED = 100;

    private int result;

    public LoginResultMessage(int result) {
        messageType = LOGIN_RESULT;
        this.result = result;
    }

    public LoginResultMessage() {
        messageType = LOGIN_RESULT;
    }

    public void writeExternal(OutputStream out) throws IOException {
        out.write(messageType);
        out.write(result);
    }

    public void readExternal(InputStream in) throws IOException {
        int a = in.read();
        result = a;

    }
}
