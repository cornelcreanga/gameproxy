package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.gateway.CustomerStorage;
import com.ccreanga.gameproxy.incoming.MatchMsg;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.util.HashMap;
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

    private CustomerStorage customerStorage;

    private ListMultimap<Long, Customer> customerForDispatch = ArrayListMultimap.create();

    public MessageDispatcher(CustomerStorage customerStorage) {
        this.customerStorage = customerStorage;
        Set<Customer> customers = customerStorage.getCustomers();
        customers.forEach(c->{
            long[] m = c.getMatches();
            for (long l : m) {
                customerForDispatch.put(l, c);
            }

        });
    }

    public List<Customer> getCustomersForDispatch(MatchMsg message) {
        return customerForDispatch.get(message.getMatchId());
//        Set<Customer> customers = customerStorage.getCustomers();
//        return customers.stream().
//            filter(customer -> customer.hasMatch(message.getMatchId())).
//            collect(Collectors.toList());

    }

}
