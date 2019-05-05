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
public class HistoryDataMsg extends ClientMsg {

    private long startTimestamp;//go back in time
    private long endTimestamp;//go back in time

    HistoryDataMsg(){
        super(HISTORICAL_DATA);
    }

    public HistoryDataMsg(long startTimestamp, long endTimestamp) {
        super(HISTORICAL_DATA);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
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
