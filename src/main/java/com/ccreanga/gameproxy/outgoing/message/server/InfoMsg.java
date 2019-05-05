package com.ccreanga.gameproxy.outgoing.message.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InfoMsg extends ServerMsg {

    public static final short INCREASE_CONSUMING_RATE = 10;
    public static final short CLOSE_CONSUMING_RATE_TOO_SLOW = 20;
    public static final short HISTORY_ALREADY_STARTED = 30;
    public static final short HISTORY_BAD_INTERVAL = 40;



    private int code;

    public InfoMsg() {
        super(INFO);
    }

    public InfoMsg(int code) {
        super(INFO);
        this.code = code;
    }

    public void writeExternal(OutputStream out) throws IOException {
        super.writeExternal(out);
        out.write(code);
    }

    public void readExternal(InputStream in) throws IOException {
        code = in.read();
    }

}
