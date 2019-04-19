package com.ccreanga.gameproxy.outgoing.message.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResultMessage extends AbstractMessage{

    public static final short AUTHORIZED = 1;
    public static final short UNAUTHORIZED = 2;

    private int result;

    public void writeExternal(OutputStream out) throws IOException {
        out.write(messageType);
        out.write(result);
    }

    public static LoginResultMessage readExternal(InputStream in) throws IOException {
        LoginResultMessage m = new LoginResultMessage();
        int a = in.read();
        m.result = a;
        return m;
    }
}
