package com.ccreanga.gameproxy.outgoing.message.server;

import java.io.IOException;
import java.io.OutputStream;

public class DataEndMsg implements ServerMsg {

    @Override
    public void writeExternal(OutputStream out) throws IOException {

    }

    @Override
    public int getType() {
        return ServerMsg.DATA_END;
    }
}
