package com.ccreanga.kafkaproducer.gateway;

import com.ccreanga.kafkaproducer.Customer;

import java.util.List;

public interface TopicStorage {

    List<String> getTopics(Long match);

    List<String> getAllTopics();

}
