package com.ccreanga.gameproxy.outgoing.message.client;

import com.ccreanga.gameproxy.util.FastDataInputStream;
import com.ccreanga.gameproxy.util.FastDataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

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

    public void writeExternal(OutputStream outputStream) throws IOException {
        FastDataOutputStream out = new FastDataOutputStream(outputStream);
        out.writeLongs(startTimestamp,endTimestamp);
    }

    public static HistoryDataMsg readExternal(InputStream inputStream) throws IOException {
        FastDataInputStream in = new FastDataInputStream(inputStream);
        HistoryDataMsg msg = new HistoryDataMsg();
        msg.startTimestamp = in.readLong();
        msg.endTimestamp = in.readLong();
        return msg;
    }

    @Override
    public int getType() {
        return ClientMsg.HISTORICAL_DATA;
    }
}
