package com.ccreanga.gameproxy.outgoing.message.client;

public class MalformedMessageException extends RuntimeException {

    private String code;

    public MalformedMessageException(String message, String code) {
        super(message);
        this.code = code;
    }
}
