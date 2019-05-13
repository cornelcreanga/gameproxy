package com.ccreanga.realtime;

import com.ccreanga.protocol.incoming.MatchMsg;
import com.ccreanga.realtime.gateway.CustomerStorage;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

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
        customers.forEach(c -> {
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
