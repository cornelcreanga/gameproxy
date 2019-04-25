package com.ccreanga.gameproxy.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

@Data
public class SendData extends ClientMessage {

    private long lastTimestamp;//go back in time

    public SendData() {
        messageType = CLIENT_SEND_DATA;
    }

    public void writeExternal(OutputStream out) throws IOException {
        out.write((int)(lastTimestamp >> 32));
        out.write((int)lastTimestamp);
    }

    public void readExternal(InputStream in) throws IOException {
        int a,b;
        a = in.read();
        b = in.read();
        lastTimestamp = (long) a << 32 | b & 0xFFFFFFFFL;
    }
}
