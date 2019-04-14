package com.ccreanga.kafkaproducer.gateway;

import com.ccreanga.kafkaproducer.Customer;
import java.util.HashSet;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class CustomerStorageMemory implements CustomerStorage {

    private Set<Customer> set = new HashSet<>();

    @Override
    public void addCustomer(Customer customer) {
        set.add(customer);
    }

    @Override
    public void removeCustomer(Customer customer) {
        set.remove(customer);
    }

    @Override
    public Set<Customer> getCustomers() {
        return Collections.unmodifiableSet(set);
    }

    @PostConstruct
    private void init(){
        set.add(new Customer("test1", Arrays.asList(1L,2L)));
        set.add(new Customer("test2", Arrays.asList(1L,2L,3L,4L,5L)));
        set.add(new Customer("test3", Arrays.asList(1L)));
        set.add(new Customer("test4", Arrays.asList(1L,2L,3L)));
        set.add(new Customer("test5", Arrays.asList(1L,2L,3L,4L,5L)));
    }
}
