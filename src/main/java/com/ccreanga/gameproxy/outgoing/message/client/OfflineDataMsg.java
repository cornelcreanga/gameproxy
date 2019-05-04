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
public class OfflineDataMsg extends ClientMsg {

    private long startTimestamp;//go back in time
    private long endTimestamp;//go back in time

    public OfflineDataMsg() {
        super(HISTORICAL_DATA);
    }

    public void writeExternal(OutputStream out) throws IOException {
        super.writeExternal(out);
        writeLong(out, startTimestamp);
        writeLong(out, endTimestamp);
    }

    public void readExternal(InputStream in) throws IOException {
        startTimestamp = readLong(in);
        endTimestamp = readLong(in);
    }
}
