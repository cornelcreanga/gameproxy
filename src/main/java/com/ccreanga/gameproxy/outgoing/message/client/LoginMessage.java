package com.ccreanga.gameproxy.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

@Data
public class LoginMessage extends ClientMessage {

    private String name;

    public LoginMessage() {
        super(CLIENT_LOGIN);
    }

    public void writeExternal(OutputStream out) throws IOException {
        super.writeExternal(out);
        byte[] b = name.getBytes();
        out.write(b.length);
        out.write(b);
    }

    public void readExternal(InputStream in) throws IOException {

        int a = in.read();
        if ((a>0) && (a<100)) {
            byte[] n = new byte[a];
            in.read(n);
            name = new String(n);
        }else{
            throw new MalformedMessageException("malformed message", "NAME_TOO_LONG");
        }
    }
}
