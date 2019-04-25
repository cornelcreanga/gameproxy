package com.ccreanga.gameproxy.outgoing.message.server;

import com.ccreanga.gameproxy.incoming.IncomingMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

@Data
public class DataMessage extends ServerMessage {

    private IncomingMessage message;

    public DataMessage(IncomingMessage message) {
        this.message = message;
    }

    public void writeExternal(OutputStream out) throws IOException {
        message.writeExternal(out);
    }

    public void readExternal(InputStream in) throws IOException {
        message = IncomingMessage.readExternal(in);
    }
}
