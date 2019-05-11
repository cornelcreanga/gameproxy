package com.ccreanga.gameproxy.outgoing.realtime;

import com.ccreanga.gameproxy.CurrentSession;
import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.CustomerSession;
import com.ccreanga.gameproxy.outgoing.message.MessageIO;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealtimeWriter implements Runnable {

    private Customer customer;
    private CurrentSession session;
    private Socket socket;
    private BlockingQueue<ServerMsg> messages;
    private volatile boolean stopped = false;

    public RealtimeWriter(CurrentSession session,Customer customer, Socket socket, BlockingQueue<ServerMsg> messages) {
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
                MessageIO.serializeServerMsg(message,out);
                out.flush();
                log.trace("Wrote the message type {} to customer {}", message.getType(), customer.getName());
            } catch (SocketException socketException) {
                if (socketException.getMessage().equals("Socket is closed")){
                    log.warn("Socket is closed for the customer {}",customer);
                    session.logout(customer); }
                else{
                    log.warn(socketException.getMessage());//todo
                }
                //todo handle exception
            } catch (IOException ioException){
                log.warn(ioException.getMessage());//todo
            } catch (InterruptedException interruptedException){
                log.warn("interrupted while waiting for message");
            }

        }
    }

    public void stop(boolean stopped) {
        this.stopped = stopped;
    }
}
