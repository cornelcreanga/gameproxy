package com.ccreanga.gameproxy.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogoutMessage extends AbstractMessage{

    public void writeExternal(OutputStream out) throws IOException {
        out.write(messageType);
    }

    public static LogoutMessage readExternal(InputStream in) throws IOException {
        LogoutMessage m = new LogoutMessage();
        m.messageType = CLIENT_LOGOUT;
        return m;
    }
}
