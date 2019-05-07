package com.ccreanga.gameproxy.incoming;

import java.io.IOException;
import java.io.OutputStream;

public class StopMsg implements IncomingMsg{

    @Override
    public void writeExternal(OutputStream out) throws IOException {

    }

    @Override
    public int getType() {
        return IncomingMsg.STOP;
    }


}
