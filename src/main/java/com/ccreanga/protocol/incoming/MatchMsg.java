package com.ccreanga.protocol.incoming;

import com.ccreanga.protocol.util.FastDataInputStream;
import com.ccreanga.protocol.util.FastDataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchMsg implements IncomingMsg{

    private UUID id;
    private long matchId;
    private byte[] message;
    private long timestamp;

    public void writeExternal(OutputStream outputStream) throws IOException {
        FastDataOutputStream out = new FastDataOutputStream(outputStream);
        out.writeUUID(id);
        out.writeLong(matchId);
        out.writeByteArray(message);
        out.writeLong(timestamp);
    }

    @Override
    public int getType() {
        return IncomingMsg.MATCH;
    }

    public static MatchMsg readExternal(InputStream inputStream) throws IOException {
        MatchMsg m = new MatchMsg();
        FastDataInputStream in = new FastDataInputStream(inputStream);
        m.id = in.readUUID();
        m.matchId = in.readLong();
        m.message=in.readByteArray();
        m.timestamp = in.readLong();
        return m;
    }

    @Override
    public String toString() {
        return
            "id=" + id +
                " matchId=" + matchId +
                " message length=" + message.length +
                " timestamp=" + timestamp;
    }
}
