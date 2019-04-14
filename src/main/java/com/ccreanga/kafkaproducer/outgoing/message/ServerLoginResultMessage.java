package com.ccreanga.kafkaproducer.outgoing.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerLoginResultMessage {

    public static final short AUTHORIZED = 1;
    public static final short UNAUTHORIZED = 2;

    private int type;

    public void writeExternal(OutputStream out) throws IOException {
        out.write(type);
    }

    public static ServerLoginResultMessage readExternal(InputStream in) throws IOException {
        ServerLoginResultMessage m = new ServerLoginResultMessage();
        int a = in.read();
        m.type = a;
        return m;
    }
}
