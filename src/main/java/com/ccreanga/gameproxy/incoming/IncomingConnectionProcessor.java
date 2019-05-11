package com.ccreanga.gameproxy.incoming;

import com.ccreanga.gameproxy.CurrentSession;
import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.CustomerSession;
import com.ccreanga.gameproxy.MessageDispatcher;
import com.ccreanga.gameproxy.kafka.KafkaMessageProducer;
import com.ccreanga.gameproxy.outgoing.message.server.DataMsg;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IncomingConnectionProcessor {

    private final CurrentSession currentSession;

    private MessageDispatcher messageDispatcher;

    private KafkaMessageProducer kafkaMessageProducer;

    public IncomingConnectionProcessor(CurrentSession currentSession, MessageDispatcher messageDispatcher,
        KafkaMessageProducer kafkaMessageProducer) {
        this.currentSession = currentSession;
        this.messageDispatcher = messageDispatcher;
        this.kafkaMessageProducer = kafkaMessageProducer;
    }

    public void handleConnection(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        IncomingMsg message;
        while(true) {//todo
            try {
                int type = input.read();
                switch(type){
                    case IncomingMsg.MATCH: message = MatchMsg.readExternal(input);break;
                    case -1:
                    case IncomingMsg.STOP: message = StopMsg.readExternal(input);break;
                    default:throw new RuntimeException("unknown message type "+type);
                }
            }catch (EOFException e){
                return;
            }
            if (log.isTraceEnabled()) {
                log.trace("IncomingMessage {}",message.toString());
            }
            if (message instanceof StopMsg)
                return;

            List<Customer> customers = messageDispatcher.getCustomersForDispatch((MatchMsg)message);
            if (log.isTraceEnabled()) {
                log.trace("Customers that are registered for this message {}", Arrays.toString(customers.toArray()));
            }
            for (Customer customer : customers) {
                kafkaMessageProducer.sendAsynchToKafka(customer.getName(), (MatchMsg)message);
                CustomerSession customerSession = currentSession.getCustomerSession(customer);
                if (customerSession == null) {
                    log.trace("Customer {} is offline", customer.getName());
                    //todo - handle history case
                } else {
                    log.trace("Customer {} is online", customer.getName());
                    BlockingQueue<ServerMsg> queue = customerSession.getMessageQueues();
                    if (queue != null) {
                        try {
                            log.trace("Add message to queue");
                            queue.add(new DataMsg((MatchMsg)message));
                        } catch (IllegalStateException e) {
                            log.warn(e.getMessage());//todo
                        }
                    }
                }
            }
        }

    }
}
