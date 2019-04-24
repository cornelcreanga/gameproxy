package com.ccreanga.gameproxy;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class CurrentSession {

    private Map<Customer,CustomerSession> customerSessions = new ConcurrentHashMap<>();

    public CustomerSession login(Customer customer, Socket socket){
        return customerSessions.putIfAbsent(customer,new CustomerSession(customer,new ArrayBlockingQueue<>(10_000_000),socket));
    }

    public void logout(Customer customer){
        customerSessions.remove(customer);
    }

    public CustomerSession getCustomerSession(Customer customer){
        return customerSessions.get(customer);
    }
}
