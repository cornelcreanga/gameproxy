package com.ccreanga.gameproxy.outgoing.message.server;

import com.ccreanga.gameproxy.incoming.MatchMsg;
import com.ccreanga.gameproxy.util.FastDataInputStream;
import com.ccreanga.gameproxy.util.FastDataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import lombok.Data;

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

    public void writeExternal(OutputStream outputStream) throws IOException {
        FastDataOutputStream out = new FastDataOutputStream(outputStream);
        out.writeUUID(id);
        out.writeLong(matchId);
        out.writeByteArray(message);
        out.writeLong(timestamp);
    }

    @Override
    public int getType() {
        return ServerMsg.DATA;
    }

    public static DataMsg readExternal(InputStream inputStream) throws IOException {
        DataMsg m = new DataMsg();
        FastDataInputStream in = new FastDataInputStream(inputStream);
        m.id = in.readUUID();
        m.matchId = in.readLong();
        m.message=in.readByteArray();
        m.timestamp = in.readLong();
        return m;
    }
}
