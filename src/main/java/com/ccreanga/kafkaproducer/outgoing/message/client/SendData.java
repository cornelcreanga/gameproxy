package com.ccreanga.kafkaproducer.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendData extends AbstractMessage{

    private int secondsInterval;//go back in time

    public void writeExternal(OutputStream out) throws IOException {
        out.write(secondsInterval);
    }

    public static SendData readExternal(InputStream in) throws IOException {
        SendData m = new SendData();
        m.messageType = CLIENT_SEND_DATA;
        m.secondsInterval = in.read();
        return m;
    }
}
