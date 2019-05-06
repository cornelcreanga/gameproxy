package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.gateway.CustomerStorage;
import com.ccreanga.gameproxy.incoming.MatchMsg;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implements the dispatching rules - what customers are going to receive a particular message
 */
@Component
public class MessageDispatcher {

    @Autowired
    private CustomerStorage customerStorage;

    public List<Customer> getCustomersForDispatch(MatchMsg message) {
        Set<Customer> customers = customerStorage.getCustomers();
        return customers.stream().
            filter(customer -> customer.hasMatch(message.getMatchId())).
            collect(Collectors.toList());

    }

}
