package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.outgoing.OutgoingMessageSender;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CurrentSession {

    @Autowired
    private OutgoingMessageSender outgoingMessageSender;

    private Map<Customer,CustomerSession> customerSessions = new ConcurrentHashMap<>();

    public CustomerSessionStatus login(Customer customer, Socket socket) {
        CustomerSession newSession = new CustomerSession(customer, new LinkedBlockingQueue<>(1_000_000), socket);
        CustomerSession customerSession = customerSessions.putIfAbsent(customer, newSession);
        if (customerSession != null) {
            return new CustomerSessionStatus(customerSession, true);
        } else {
            outgoingMessageSender.createConsumer(customer, socket, newSession.getMessageQueues());
            log.trace("Created consumer thread for customer {}", customer.getName());
            return new CustomerSessionStatus(customerSession, false);
        }
    }

    public void logout(Customer customer){
        log.info("Customer name={} logout", customer.getName());
        customerSessions.remove(customer);
    }

    public CustomerSession getCustomerSession(Customer customer){
        return customerSessions.get(customer);
    }
}
