package com.ccreanga.kafkaproducer;

import lombok.Value;

import java.util.List;


@Value
public class Customer{
    private String name;
    private List<Long> matches;

}
