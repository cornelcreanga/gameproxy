package com.ccreanga.gameproxy.outgoing.message.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataEndMsg implements ServerMsg {

    @Override
    public void writeExternal(OutputStream out) throws IOException {

    }


    public static DataEndMsg readExternal(InputStream in) throws IOException {
        DataEndMsg dataEndMsg = new DataEndMsg();
        return dataEndMsg;
    }

    @Override
    public int getType() {
        return ServerMsg.DATA_END;
    }
}
