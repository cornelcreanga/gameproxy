package com.ccreanga.gameproxy.gateway;

import com.ccreanga.gameproxy.Customer;

import java.util.Set;

public interface CustomerStorage {

    void addCustomer(Customer customer);
    void removeCustomer(Customer customer);
    Set<Customer> getCustomers();

}
