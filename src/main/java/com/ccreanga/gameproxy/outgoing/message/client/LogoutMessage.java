package com.ccreanga.gameproxy.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutMessage extends AbstractMessage{

    private String name;

    public void writeExternal(OutputStream out) throws IOException {
        out.write(messageType);
        byte[] b = name.getBytes();
        out.write(b.length);
        out.write(b);
    }

    public static LogoutMessage readExternal(InputStream in) throws IOException {
        LogoutMessage m = new LogoutMessage();
        m.messageType = CLIENT_LOGOUT;
        int a = in.read();

        if ((a>0) && (a<100)) {
            byte[] n = new byte[a];
            in.read(n);
            m.name = new String(n);
        }
        return m;
    }
}
