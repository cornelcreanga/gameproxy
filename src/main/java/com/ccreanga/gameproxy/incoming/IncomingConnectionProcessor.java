package com.ccreanga.gameproxy.incoming;

import com.ccreanga.gameproxy.CurrentSession;
import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.CustomerSession;
import com.ccreanga.gameproxy.MessageDispatcher;
import com.ccreanga.gameproxy.kafka.KafkaMessageProducer;
import com.ccreanga.gameproxy.outgoing.message.server.DataMsg;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IncomingConnectionProcessor {

    @Autowired
    private CurrentSession currentSession;

    @Autowired
    MessageDispatcher messageDispatcher;

    @Autowired
    KafkaMessageProducer kafkaMessageProducer;

    public void handleConnection(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        IncomingMsg message = IncomingMsg.readExternal(input);
        log.trace("IncomingMessage " + message.toString());

        List<Customer> customers = messageDispatcher.getCustomers(message);
        if (log.isTraceEnabled()) {
            log.trace("Session customers that will receive the message {}", Arrays.toString(customers.toArray()));
        }
        for (Customer customer : customers) {
            kafkaMessageProducer.sendAsynchToKafka(customer.getName(), message);
            CustomerSession customerSession = currentSession.getCustomerSession(customer);
            if (customerSession == null) {
                log.trace("Customer {} is history", customer.getName());
                //todo - handle history case
            } else {
                log.trace("Customer {} is online", customer.getName());
                BlockingQueue<ServerMsg> queue = customerSession.getMessageQueues();
                if (queue != null) {
                    try {
                        log.trace("Add message to queue");
                        queue.add(new DataMsg(message));
                    } catch (IllegalStateException e) {
                        //todo - queue is full, handle this case
                    }
                }
            }
        }

    }
}
