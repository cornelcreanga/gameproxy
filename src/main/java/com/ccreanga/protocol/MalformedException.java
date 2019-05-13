package com.ccreanga.protocol;

public class MalformedException extends RuntimeException {

    private String code;

    public MalformedException(String message, String code) {
        super(message);
        this.code = code;
    }
}
