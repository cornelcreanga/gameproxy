package com.ccreanga.gameproxy.outgoing.message.server;

import com.ccreanga.gameproxy.incoming.IncomingMsg;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataMsg extends ServerMsg {

    private IncomingMsg message;

    public DataMsg() {
        super(DATA);
    }

    public DataMsg(IncomingMsg message) {
        super(DATA);
        this.message = message;
    }

    public void writeExternal(OutputStream out) throws IOException {
        super.writeExternal(out);
        message.writeExternal(out);
    }

    public void readExternal(InputStream in) throws IOException {
        message = IncomingMsg.readExternal(in);
    }
}
