package com.ccreanga.gameproxy.outgoing.message.client;

import static com.ccreanga.gameproxy.util.IOUtil.readLong;
import static com.ccreanga.gameproxy.util.IOUtil.writeLong;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SendDataMsg extends ClientMsg {

    private long lastTimestamp;//go back in time

    public SendDataMsg() {
        super(CLIENT_SEND_DATA);
    }

    public void writeExternal(OutputStream out) throws IOException {
        super.writeExternal(out);
        writeLong(out, lastTimestamp);
    }

    public void readExternal(InputStream in) throws IOException {
        lastTimestamp = readLong(in);
    }
}
