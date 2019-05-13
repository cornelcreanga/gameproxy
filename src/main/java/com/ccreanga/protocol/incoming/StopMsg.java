package com.ccreanga.protocol.incoming;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StopMsg implements IncomingMsg{

    @Override
    public void writeExternal(OutputStream out) throws IOException {

    }

    public static StopMsg readExternal(InputStream inputStream) throws IOException {
        return new StopMsg();
    }

    @Override
    public int getType() {
        return IncomingMsg.STOP;
    }


}
