package com.ccreanga.realtime.outgoing.realtime;

import com.ccreanga.protocol.outgoing.MessageIO;
import com.ccreanga.protocol.outgoing.server.ServerMsg;
import com.ccreanga.realtime.CurrentSession;
import com.ccreanga.realtime.Customer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class RealtimeWriter implements Runnable {

    private Customer customer;
    private CurrentSession session;
    private Socket socket;
    private BlockingQueue<ServerMsg> messages;
    private volatile boolean stopped = false;

    public RealtimeWriter(CurrentSession session, Customer customer, Socket socket, BlockingQueue<ServerMsg> messages) {
        this.session = session;
        this.customer = customer;
        this.socket = socket;
        this.messages = messages;
    }

    @Override
    public void run() {
        while (!stopped) {
            try {
                ServerMsg message = messages.take();
                log.trace("Consumed the message type {} from the queue", message.getType());
                OutputStream out = socket.getOutputStream();
                MessageIO.serializeServerMsg(message, out);
                log.trace("Wrote the message type {} to customer {}", message.getType(), customer.getName());
            } catch (SocketException socketException) {
                String m = socketException.getMessage();
                if (m.equals("Socket is closed") || (m.contains("Broken pipe"))) {
                    log.warn("Socket is closed for the customer {}", customer.getName());
                    session.logout(customer);
                } else {
                    log.warn("SocketException " + m);//todo
                }
                stopped = true;
                //todo handle exception
            } catch (IOException ioException) {
                log.warn("IOException " + ioException.getMessage());//todo
                stopped = true;
            } catch (InterruptedException interruptedException) {
                log.warn("interrupted while waiting for message");
            }

        }
    }

    public void stop(boolean stopped) {
        this.stopped = stopped;
    }
}
