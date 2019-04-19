package com.ccreanga.gameproxy.outgoing.message.client;

import lombok.Data;

@Data
public abstract class AbstractMessage {

    public static final short CLIENT_LOGIN = 1;
    public static final short CLIENT_LOGOUT = 2;
    public static final short CLIENT_SEND_DATA = 3;

    protected int messageType;
}
