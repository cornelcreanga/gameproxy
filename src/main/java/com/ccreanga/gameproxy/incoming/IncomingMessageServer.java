package com.ccreanga.gameproxy.incoming;

import com.ccreanga.gameproxy.CurrentSession;
import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.CustomerSession;
import com.ccreanga.gameproxy.MessageDispatcher;
import com.ccreanga.gameproxy.ServerConfig;
import com.ccreanga.gameproxy.kafka.KafkaMessageProducer;
import com.ccreanga.gameproxy.outgoing.message.server.DataMessage;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMessage;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IncomingMessageServer implements Runnable {

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private CurrentSession currentSession;

    @Autowired
    MessageDispatcher messageDispatcher;

    @Autowired
    KafkaMessageProducer kafkaMessageProducer;


    private ServerSocket serverSocket = null;
    private boolean isStopped = false;


    public void run() {
        try {
            serverSocket = new ServerSocket(serverConfig.getIncomingPort());
            log.info("incoming server started on {}", serverConfig.getIncomingPort());
            while (!isStopped) {
                Socket clientSocket = serverSocket.accept();
                InputStream input = clientSocket.getInputStream();
                IncomingMessage message = IncomingMessage.readExternal(input);
                log.trace("IncomingMessage " + message.toString());

                List<Customer> customers = messageDispatcher.getCustomers(message);
                if (log.isTraceEnabled()) {
                    log.trace("Session customers that will receive the message {}", Arrays.toString(customers.toArray()));
                }
                for (Customer customer : customers) {
                    kafkaMessageProducer.sendAsynchToKafka(customer.getName(), message);
                    CustomerSession customerSession = currentSession.getCustomerSession(customer);
                    if (customerSession==null){
                        log.trace("Customer {} is offline", customer.getName());
                        //todo - handle offline case
                    }else {
                        log.trace("Customer {} is online", customer.getName());
                        BlockingQueue<ServerMessage> queue = customerSession.getMessageQueues();
                        if (queue != null) {
                            try {
                                log.trace("Add message to queue");
                                queue.add(new DataMessage(message));
                            } catch (IllegalStateException e) {
                                //todo - queue is full, handle this case
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Server Stopped.");
    }

    public synchronized void stop() {
        isStopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }


}
