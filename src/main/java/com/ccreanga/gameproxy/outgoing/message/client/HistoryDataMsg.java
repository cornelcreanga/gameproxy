package com.ccreanga.gameproxy.outgoing.message.client;

import static com.ccreanga.gameproxy.util.IOUtil.readLong;
import static com.ccreanga.gameproxy.util.IOUtil.writeLong;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class HistoryDataMsg implements ClientMsg {

    private long startTimestamp;//go back in time
    private long endTimestamp;//go back in time

    HistoryDataMsg(){
    }

    public HistoryDataMsg(long startTimestamp, long endTimestamp) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public void writeExternal(OutputStream out) throws IOException {
        writeLong(out, startTimestamp);
        writeLong(out, endTimestamp);
    }

    public static HistoryDataMsg readExternal(InputStream in) throws IOException {
        HistoryDataMsg msg = new HistoryDataMsg();
        msg.startTimestamp = readLong(in);
        msg.endTimestamp = readLong(in);
        return msg;
    }

    @Override
    public int getType() {
        return ClientMsg.HISTORICAL_DATA;
    }
}
