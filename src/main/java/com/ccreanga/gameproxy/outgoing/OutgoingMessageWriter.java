package com.ccreanga.gameproxy.outgoing;

import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutgoingMessageWriter implements Runnable {

    private Customer customer;
    private Socket socket;
    private BlockingQueue<ServerMsg> messages;
    private volatile boolean stopped = false;


    public OutgoingMessageWriter(Customer customer, Socket socket, BlockingQueue<ServerMsg> messages) {
        this.customer = customer;
        this.socket = socket;
        this.messages = messages;
    }

    @Override
    public void run() {
        while (!stopped) {
            ServerMsg message = null;
            try {
                message = messages.take();
                log.trace("Consumed the message type {} from the queue", message.getType());
                OutputStream out = socket.getOutputStream();
                message.writeExternal(out);
                out.flush();
                log.trace("Wrote the message type {} to customer {}", message.getType(), customer.getName());
            } catch (Exception e) {
                //todo handle exception
            }

        }
    }

    public void stop(boolean stopped) {
        this.stopped = stopped;
    }
}
