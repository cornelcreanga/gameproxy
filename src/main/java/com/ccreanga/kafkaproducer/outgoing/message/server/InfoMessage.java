package com.ccreanga.kafkaproducer.outgoing.message.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoMessage extends AbstractMessage{
    public static final short NOT_ALLOWED = 1;
    public static final short ALREADY_REGISTERED = 2;

    private int info;

    public void writeExternal(OutputStream out) throws IOException {
        out.write(messageType);
        out.write(info);
    }

    public static InfoMessage readExternal(InputStream in) throws IOException {
        InfoMessage m = new InfoMessage();
        int a = in.read();
        m.info = a;
        return m;
    }
}
