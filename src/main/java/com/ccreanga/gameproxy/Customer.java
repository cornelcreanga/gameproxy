package com.ccreanga.gameproxy;

import java.util.Arrays;
import java.util.TreeSet;
import lombok.Value;

import java.util.List;


@Value
public class Customer{
    private String name;
    private long[] matches;

    public boolean hasMatch(long match){
        if (matches.length==1){
            return matches[0]==match;
        }
        return Arrays.binarySearch(matches,match)!=-1;
    }

}
