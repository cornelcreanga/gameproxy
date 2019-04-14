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
public class ClientLoginMessage {

    private String name;

    public void writeExternal(OutputStream out) throws IOException {
        byte[] b = name.getBytes();
        out.write(b.length);
        out.write(b);
    }

    public static ClientLoginMessage readExternal(InputStream in) throws IOException {
        ClientLoginMessage m = new ClientLoginMessage();
        int a = in.read();
        if ((a>0) && (a<100)) {
            byte[] n = new byte[a];
            in.read(n);
            m.name = new String(n);
        }
        return m;
    }
}
