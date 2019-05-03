package com.ccreanga.gameproxy.outgoing.message.client;

import java.io.IOException;
import java.io.InputStream;

public class ClientMsgFactory {

    public static LoginMsg loginMsg(InputStream in) throws IOException {
        LoginMsg message = new LoginMsg();
        message.readExternal(in);
        return message;
    }

    public static LogoutMsg logoutMsg(InputStream in) throws IOException {
        LogoutMsg message = new LogoutMsg();
        message.readExternal(in);
        return message;
    }

    public static OfflineDataMsg sendDataMsg(InputStream in) throws IOException {
        OfflineDataMsg message = new OfflineDataMsg();
        message.readExternal(in);
        return message;
    }

}
