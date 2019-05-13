package com.ccreanga.protocol.incoming;

import java.io.IOException;
import java.io.OutputStream;

public interface IncomingMsg {

    public static final short MATCH = 1;
    public static final short STOP = 2;

    public void writeExternal(OutputStream out) throws IOException;

    public int getType();

}
