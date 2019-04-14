package com.ccreanga.kafkaproducer.outgoing.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandMessage {

    public static final short START = 1;
    public static final short CLOSE = 2;
    public static final short RESUME = 3;

    private int type;
    private long secondsInterval;//go back in time

}
