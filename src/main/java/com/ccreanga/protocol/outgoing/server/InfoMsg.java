package com.ccreanga.protocol.outgoing.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InfoMsg implements ServerMsg {

    public static final short INCREASE_CONSUMING_RATE = 10;
    public static final short CLOSE_CONSUMING_RATE_TOO_SLOW = 20;
    public static final short HISTORY_ALREADY_STARTED = 30;
    public static final short HISTORY_BAD_INTERVAL = 40;

    public static final short AUTHORIZED = 100;
    public static final short ALREADY_AUTHENTICATED = 110;
    public static final short UNAUTHORIZED = 120;

    private int code;

    private InfoMsg(){

    }

    public InfoMsg(int code) {
        this.code = code;
    }

    public void writeExternal(OutputStream out) throws IOException {
        out.write(code);
    }

    @Override
    public int getType() {
        return ServerMsg.INFO;
    }

    public int getCode() {
        return code;
    }

    public static InfoMsg readExternal(InputStream in) throws IOException {
        InfoMsg infoMsg = new InfoMsg();

        infoMsg.code = in.read();
        return infoMsg;
    }

}
