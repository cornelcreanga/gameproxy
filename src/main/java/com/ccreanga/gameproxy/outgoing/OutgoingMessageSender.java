package com.ccreanga.gameproxy.outgoing;

import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMessage;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class OutgoingMessageSender {


    private ExecutorService service = new ThreadPoolExecutor(16, 64, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));
    private Map<Customer, OutgoingMessageWriter> writers = new ConcurrentHashMap<>();

    public void createConsumer(Customer customer, Socket socket, BlockingQueue<ServerMessage> messages) {
        OutgoingMessageWriter outgoingMessageWriter = new OutgoingMessageWriter(socket, messages);
        writers.put(customer, outgoingMessageWriter);
        service.submit(outgoingMessageWriter);
    }

    //todo - it should manage the threads writing data to the customers
}
