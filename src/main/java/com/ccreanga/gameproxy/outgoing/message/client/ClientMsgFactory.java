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

    public static HistoryDataMsg sendDataMsg(InputStream in) throws IOException {
        HistoryDataMsg message = new HistoryDataMsg();
        message.readExternal(in);
        return message;
    }

}
