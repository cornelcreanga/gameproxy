package com.ccreanga.kafkaproducer.outgoing.message.server;

public class AbstractMessage {

    public static final short LOGIN_RESULT = 1;
    public static final short DATA = 2;
    public static final short INFO = 3;

    protected int messageType;

}
