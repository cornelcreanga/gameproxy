package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.gateway.CustomerStorage;
import com.ccreanga.gameproxy.incoming.IncomingMessage;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageDispatcher {

    @Autowired
    private CustomerStorage customerStorage;

    public List<Customer> getCustomers(IncomingMessage message){
        Set<Customer> customers = customerStorage.getCustomers();
        return customers.stream().
            filter(customer -> customer.hasMatch(message.getMatchId())).
            collect(Collectors.toList());

    }

}
