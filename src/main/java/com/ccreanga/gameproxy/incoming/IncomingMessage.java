package com.ccreanga.gameproxy.incoming;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomingMessage {

    private long id;
    private long matchId;
    private byte[] message;
    private long timestamp;

    public void writeExternal(OutputStream out) throws IOException {
        out.write((int)(id >> 32));
        out.write((int)id);

        out.write((int)(matchId >> 32));
        out.write((int)matchId);

        if (message!=null){
            out.write(message.length);
            out.write(message);
        }else{
            out.write(0);
        }


        out.write((int)(timestamp >> 32));
        out.write((int)timestamp);
    }

    public static IncomingMessage readExternal(InputStream in) throws IOException {
        IncomingMessage m = new IncomingMessage();
        int a,b;
        a = in.read();
        b = in.read();
        m.id = (long)a << 32 | b & 0xFFFFFFFFL;
        a = in.read();
        b = in.read();
        m.matchId = (long)a << 32 | b & 0xFFFFFFFFL;
        a = in.read();
        if (a!=0) {
            m.message = new byte[a];
            in.read(m.message);
        }
        a = in.read();
        b = in.read();
        m.timestamp = (long)a << 32 | b & 0xFFFFFFFFL;
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
