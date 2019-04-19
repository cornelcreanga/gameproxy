package com.ccreanga.gameproxy.gateway;

import com.ccreanga.gameproxy.Customer;
import java.util.HashSet;
import java.util.TreeSet;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

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
        set.add(new Customer("test1", arrayToSet(1,2)));
        set.add(new Customer("test2", arrayToSet(1,2,3,4,5)));
        set.add(new Customer("test3", arrayToSet(1)));
        set.add(new Customer("test4", arrayToSet(1,2,3)));
        set.add(new Customer("test5", arrayToSet(1,2,3,4,5)));
    }

    private TreeSet<Long> arrayToSet(int... elements){
        TreeSet<Long> set = new TreeSet<>();
        for (int i = 0; i < elements.length; i++) {
            set.add((long)elements[i]);
        }
        return set;
    }
}
