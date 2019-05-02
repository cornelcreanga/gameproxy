package com.ccreanga.gameproxy.incoming;

import static com.ccreanga.gameproxy.util.IOUtil.readFully;
import static com.ccreanga.gameproxy.util.IOUtil.readLong;
import static com.ccreanga.gameproxy.util.IOUtil.writeLong;

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
public class IncomingMsg {

    private UUID id;
    private long matchId;
    private byte[] message;
    private long timestamp;

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

    public static IncomingMsg readExternal(InputStream in) throws IOException {
        IncomingMsg m = new IncomingMsg();
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

    @Override
    public String toString() {
        return
            "id=" + id +
                " matchId=" + matchId +
                " message length=" + message.length +
                " timestamp=" + timestamp;
    }
}
