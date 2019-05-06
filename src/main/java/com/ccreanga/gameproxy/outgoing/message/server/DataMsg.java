package com.ccreanga.gameproxy.outgoing.message.server;

import com.ccreanga.gameproxy.incoming.MatchMsg;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import lombok.Data;

import static com.ccreanga.gameproxy.util.IOUtil.*;
import static com.ccreanga.gameproxy.util.IOUtil.writeLong;

@Data
public class DataMsg implements ServerMsg {

    private UUID id;
    private long matchId;
    private byte[] message;
    private long timestamp;

    private DataMsg() {
    }

    public DataMsg(MatchMsg msg) {
        id = msg.getId();
        matchId = msg.getMatchId();
        message = msg.getMessage();
        timestamp = msg.getTimestamp();
    }

    public void writeExternal(OutputStream out) throws IOException {
        long msb = id.getMostSignificantBits();
        long lsb = id.getLeastSignificantBits();
        writeLong(out, msb);
        writeLong(out, lsb);

        writeLong(out, matchId);

        if (message!=null){
            out.write(message.length);
            out.write(message);
        }else{
            out.write(0);
        }

        writeLong(out, timestamp);
    }

    @Override
    public int getType() {
        return ServerMsg.DATA;
    }

    public static DataMsg readExternal(InputStream in) throws IOException {
        DataMsg m = new DataMsg();
        long msb, lsb;

        msb = readLong(in);
        lsb = readLong(in);
        m.id = new UUID(msb, lsb);
        m.matchId = readLong(in);
        int a = in.read();
        if (a!=0) {
            m.message = new byte[a];
            readFully(in, m.message);
        }
        m.timestamp = readLong(in);
        return m;
    }
}
