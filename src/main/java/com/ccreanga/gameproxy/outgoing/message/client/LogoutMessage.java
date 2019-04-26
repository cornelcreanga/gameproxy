package com.ccreanga.gameproxy.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

@Data
public class LogoutMessage extends ClientMessage {

    public LogoutMessage() {
        super(CLIENT_LOGOUT);
    }

    public void writeExternal(OutputStream out) throws IOException {
        super.writeExternal(out);
    }

    public void readExternal(InputStream in) throws IOException {
    }
}
