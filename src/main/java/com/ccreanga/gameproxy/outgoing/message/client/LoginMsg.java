package com.ccreanga.gameproxy.outgoing.message.client;

import static com.ccreanga.gameproxy.util.IOUtil.readFully;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginMsg extends ClientMsg {

    private String name;

    public LoginMsg() {
        super(LOGIN);
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
            readFully(in, n);
            name = new String(n);
        }else{
            throw new MalformedException("message too long " + a, "NAME_TOO_LONG");
        }
    }
}
