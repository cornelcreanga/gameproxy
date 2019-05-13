package com.ccreanga.protocol.outgoing.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

@Data
public class LogoutMsg implements ClientMsg {

    public LogoutMsg(){
    }

    public void writeExternal(OutputStream outputStream) throws IOException {
    }

    public static LogoutMsg readExternal(InputStream inputStream) throws IOException {
        return new LogoutMsg();

    }

    @Override
    public int getType() {
        return ClientMsg.LOGOUT;
    }

}
