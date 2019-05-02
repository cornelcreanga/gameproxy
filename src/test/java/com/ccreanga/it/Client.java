package com.ccreanga.it;

import static com.ccreanga.gameproxy.outgoing.message.server.ServerMsg.DATA;
import static com.ccreanga.gameproxy.outgoing.message.server.ServerMsg.LOGIN_RESULT;
import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.client.LoginMsg;
import com.ccreanga.gameproxy.outgoing.message.client.LogoutMsg;
import com.ccreanga.gameproxy.outgoing.message.server.DataMsg;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMsg;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    private String name;
    private Socket socket;
    private OutputStream out;
    private InputStream in;

    public Client(String name, String ip, int port) {
        this.name = name;
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(50000);
            out = socket.getOutputStream();
            in = socket.getInputStream();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public LoginResultMsg login() {
        try {
            LoginMsg message = new LoginMsg();
            message.setName(name);
            message.writeExternal(out);
            int messageType = in.read();
            assertEquals(messageType, LOGIN_RESULT);
            LoginResultMsg ackMessage = new LoginResultMsg();
            ackMessage.readExternal(in);
            return ackMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void logout() {
        try {
            LogoutMsg message = new LogoutMsg();
            message.writeExternal(out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public DataMsg readMessage() {
        try {

            int messageType = in.read();
            assertEquals(messageType, DATA);
            DataMsg dataMessage = new DataMsg();
            dataMessage.readExternal(in);
            return dataMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
