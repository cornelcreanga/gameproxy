package com.ccreanga.it;

import com.ccreanga.gameproxy.incoming.IncomingMsg;
import com.ccreanga.gameproxy.incoming.MatchMsg;
import com.ccreanga.gameproxy.incoming.StopMsg;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

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

    public void produce(UUID id, long matchId, String message) throws IOException {
        produce(id,matchId,message,System.currentTimeMillis());
    }

    public void produce(UUID id, long matchId, String message,long timestamp) throws IOException {
        MatchMsg incomingMessage = new MatchMsg(id, matchId, message.getBytes(), timestamp);
        out.write(IncomingMsg.MATCH);
        incomingMessage.writeExternal(out);
    }

    public void close() throws IOException{
        StopMsg stopMsg = new StopMsg();
        out.write(IncomingMsg.STOP);
        stopMsg.writeExternal(out);
        out.flush();
    }

}



