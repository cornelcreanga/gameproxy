package com.ccreanga.gameproxy.outgoing.message.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResultMsg extends ServerMsg {

    public static final short AUTHORIZED = 10;
    public static final short ALREADY_AUTHENTICATED = 20;
    public static final short UNAUTHORIZED = 100;

    private int result;

    public LoginResultMsg() {
        super(LOGIN_RESULT);
    }

    public LoginResultMsg(int result) {
        super(LOGIN_RESULT);
        this.result = result;
    }

    public void writeExternal(OutputStream out) throws IOException {
        super.writeExternal(out);
        out.write(result);
    }

    public void readExternal(InputStream in) throws IOException {
        result = in.read();
    }
}
