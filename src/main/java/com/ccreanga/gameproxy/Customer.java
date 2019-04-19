package com.ccreanga.gameproxy;

import java.util.TreeSet;
import lombok.Value;

import java.util.List;


@Value
public class Customer{
    private String name;
    private TreeSet<Long> matches;

}
