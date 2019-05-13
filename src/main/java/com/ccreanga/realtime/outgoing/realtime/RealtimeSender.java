package com.ccreanga.realtime.outgoing.realtime;

import com.ccreanga.protocol.outgoing.server.ServerMsg;
import com.ccreanga.realtime.CurrentSession;
import com.ccreanga.realtime.Customer;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class RealtimeSender {

    private ExecutorService service = new ThreadPoolExecutor(16, 64, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));
    private Map<Customer, RealtimeWriter> writers = new ConcurrentHashMap<>();

    public void createConsumer(CurrentSession session, Customer customer, Socket socket, BlockingQueue<ServerMsg> messages) {
        RealtimeWriter outgoingWriter = new RealtimeWriter(session, customer, socket, messages);
        writers.put(customer, outgoingWriter);
        service.submit(outgoingWriter);
    }

    //todo - it should manage the threads writing data to the customers
}
