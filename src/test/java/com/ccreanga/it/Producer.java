package com.ccreanga.it;

import com.ccreanga.gameproxy.incoming.IncomingMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Producer {

    private Socket socket;
    private OutputStream out;
    private InputStream in;

    public Producer(String ip, int port) {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(5000);
            out = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void produce(long id, long matchId, String message) throws IOException {
        IncomingMessage incomingMessage = new IncomingMessage(id, matchId, message.getBytes(), System.currentTimeMillis());
        incomingMessage.writeExternal(out);
    }
}



