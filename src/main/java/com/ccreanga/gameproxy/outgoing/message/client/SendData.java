package com.ccreanga.gameproxy.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendData extends AbstractMessage{

    private long lastTimestamp;//go back in time

    public void writeExternal(OutputStream out) throws IOException {
        out.write((int)(lastTimestamp >> 32));
        out.write((int)lastTimestamp);
    }

    public static SendData readExternal(InputStream in) throws IOException {
        SendData m = new SendData();
        m.messageType = CLIENT_SEND_DATA;

        int a,b;
        a = in.read();
        b = in.read();
        m.lastTimestamp = (long)a << 32 | b & 0xFFFFFFFFL;

        return m;
    }
}
