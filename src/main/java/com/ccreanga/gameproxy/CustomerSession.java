package com.ccreanga.gameproxy;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSession {

    private List<Customer> customers;
    private HashMap<Customer,ArrayBlockingQueue> messageQueues;

}
