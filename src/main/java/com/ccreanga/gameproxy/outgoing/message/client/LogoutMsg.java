package com.ccreanga.gameproxy.outgoing.message.client;

import com.ccreanga.gameproxy.util.FastDataInputStream;
import com.ccreanga.gameproxy.util.FastDataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

@Data
public class LogoutMsg implements ClientMsg {

    private String name;

    private LogoutMsg(){
    }

    public LogoutMsg(String name) {
        this.name = name;
    }

    public void writeExternal(OutputStream outputStream) throws IOException {
        FastDataOutputStream out = new FastDataOutputStream(outputStream);
        out.writeString(name,"UTF-8");
    }

    public static LogoutMsg readExternal(InputStream inputStream) throws IOException {
        FastDataInputStream in = new FastDataInputStream(inputStream);
        LogoutMsg msg = new LogoutMsg();
        msg.name = in.readString("UTF-8");
        //todo throw new MalformedException("message too long " + a, "NAME_TOO_LONG");
        return msg;

    }

    @Override
    public int getType() {
        return ClientMsg.LOGOUT;
    }

}
