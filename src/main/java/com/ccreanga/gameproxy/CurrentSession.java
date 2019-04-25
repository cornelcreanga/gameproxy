package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.outgoing.OutgoingMessageSender;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrentSession {

    @Autowired
    private OutgoingMessageSender outgoingMessageSender;

    private Map<Customer,CustomerSession> customerSessions = new ConcurrentHashMap<>();

    public CustomerSession login(Customer customer, Socket socket){
        CustomerSession newSession = new CustomerSession(customer, new LinkedBlockingQueue<>(10_000_000), socket);
        CustomerSession customerSession = customerSessions.putIfAbsent(customer, newSession);
        if (customerSession != null) {
            return customerSession;
        }

        outgoingMessageSender.createConsumer(customer, socket, newSession.getMessageQueues());
        return null;
    }

    public void logout(Customer customer){
        customerSessions.remove(customer);
    }

    public CustomerSession getCustomerSession(Customer customer){
        return customerSessions.get(customer);
    }
}
