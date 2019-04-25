package com.ccreanga.gameproxy.outgoing;

import com.ccreanga.gameproxy.outgoing.message.server.ServerMessage;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class OutgoingMessageWriter implements Runnable {

    private Socket socket;
    private BlockingQueue<ServerMessage> messages;
    private volatile boolean stopped = false;


    public OutgoingMessageWriter(Socket socket, BlockingQueue<ServerMessage> messages) {
        this.socket = socket;
        this.messages = messages;
    }

    @Override
    public void run() {
        while (!stopped) {
            ServerMessage message = null;
            try {
                message = messages.take();
                message.writeExternal(socket.getOutputStream());
            } catch (Exception e) {
                //todo handle exception
            }

        }
    }

    public void stop(boolean stopped) {
        this.stopped = stopped;
    }
}
