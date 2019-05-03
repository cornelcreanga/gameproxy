package com.ccreanga.gameproxy.outgoing.history;

import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;
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
public class HistorySender {


    private ExecutorService service = new ThreadPoolExecutor(16, 64, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));
    private Map<Customer, HistoryWriter> writers = new ConcurrentHashMap<>();

    public void createConsumer(Customer customer, Socket socket, BlockingQueue<ServerMsg> messages) {
        HistoryWriter outgoingWriter = new HistoryWriter(customer, socket, messages);
        writers.put(customer, outgoingWriter);
        service.submit(outgoingWriter);
    }

    //todo - it should manage the threads writing data to the customers
}
