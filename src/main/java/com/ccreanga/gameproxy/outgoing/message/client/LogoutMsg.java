package com.ccreanga.gameproxy.outgoing.message.client;

import static com.ccreanga.gameproxy.util.IOUtil.readFully;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class LogoutMsg implements ClientMsg {

    private String name;

    private LogoutMsg(){
    }

    public LogoutMsg(String name) {
        this.name = name;
    }

    public void writeExternal(OutputStream out) throws IOException {
        byte[] b = name.getBytes();
        out.write(b.length);
        out.write(b);
    }

    public static LogoutMsg readExternal(InputStream in) throws IOException {
        LogoutMsg msg = new LogoutMsg();
        int a = in.read();
        if ((a>0) && (a<100)) {
            byte[] n = new byte[a];
            readFully(in, n);
            msg.name = new String(n);
        }else{
            throw new MalformedException("message too long " + a, "NAME_TOO_LONG");
        }
        return msg;
    }

    @Override
    public int getType() {
        return ClientMsg.LOGOUT;
    }

}
