package com.ccreanga.kafkaproducer.gateway;

import com.ccreanga.kafkaproducer.Customer;

import java.util.Set;

public interface CustomerStorage {

    void addCustomer(Customer customer);
    void removeCustomer(Customer customer);
    Set<Customer> getCustomers();

}
