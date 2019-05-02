package com.ccreanga.gameproxy.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LogoutMsg extends ClientMsg {

    public LogoutMsg() {
        super(CLIENT_LOGOUT);
    }

    public void writeExternal(OutputStream out) throws IOException {
        super.writeExternal(out);
    }

    public void readExternal(InputStream in) throws IOException {
    }
}
