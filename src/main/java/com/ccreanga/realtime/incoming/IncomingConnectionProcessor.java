package com.ccreanga.realtime.incoming;

import com.ccreanga.protocol.MalformedException;
import com.ccreanga.protocol.incoming.IncomingMsg;
import com.ccreanga.protocol.incoming.MatchMsg;
import com.ccreanga.protocol.incoming.StopMsg;
import com.ccreanga.protocol.outgoing.server.DataMsg;
import com.ccreanga.protocol.outgoing.server.ServerMsg;
import com.ccreanga.realtime.CurrentSession;
import com.ccreanga.realtime.Customer;
import com.ccreanga.realtime.CustomerSession;
import com.ccreanga.realtime.MessageDispatcher;
import com.ccreanga.realtime.kafka.KafkaMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

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
        while (true) {//todo
            try {
                int type = input.read();
                switch (type) {
                    case IncomingMsg.MATCH:
                        message = MatchMsg.readExternal(input);
                        break;
                    case -1://socket close
                    case IncomingMsg.STOP:
                        message = StopMsg.readExternal(input);
                        break;
                    default:
                        throw new MalformedException("unknown message type " + type,"INCOMING_UNKNOWN_TYPE");
                }
            } catch (EOFException e) {
                return;
            }
            if (log.isTraceEnabled()) {
                log.trace("IncomingMessage {}", message.toString());
            }
            if (message instanceof StopMsg)
                return;

            List<Customer> customers = messageDispatcher.getCustomersForDispatch((MatchMsg) message);
            if (log.isTraceEnabled()) {
                log.trace("Customers that are registered for this message {}", Arrays.toString(customers.toArray()));
            }
            for (Customer customer : customers) {
                String name = customer.getName();
                //todo -comment for the moment
//                kafkaMessageProducer.sendAsynchToKafka(customer.getName(), (MatchMsg)message);
                CustomerSession customerSession = currentSession.getCustomerSession(customer);
                if (customerSession == null) {
                    log.trace("Customer {} is offline", name);
                } else {
                    log.trace("Customer {} is online", name);
                    BlockingQueue<ServerMsg> queue = customerSession.getMessageQueues();
                    if (queue != null) {
                        try {
                            log.trace("Add message to queue");
                            queue.add(new DataMsg((MatchMsg) message));
                        } catch (IllegalStateException e) {
                            log.warn("queue is full for customer {}",name);
                        }
                    }
                }
            }
        }

    }
}
