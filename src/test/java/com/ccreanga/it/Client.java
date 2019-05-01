package com.ccreanga.it;

import static com.ccreanga.gameproxy.outgoing.message.server.ServerMessage.DATA;
import static com.ccreanga.gameproxy.outgoing.message.server.ServerMessage.LOGIN_RESULT;
import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.client.LoginMessage;
import com.ccreanga.gameproxy.outgoing.message.server.DataMessage;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMessage;
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

    public LoginResultMessage login() {
        try {
            LoginMessage message = new LoginMessage();
            message.setName(name);
            message.writeExternal(out);
            int messageType = in.read();
            assertEquals(messageType, LOGIN_RESULT);
            LoginResultMessage ackMessage = new LoginResultMessage();
            ackMessage.readExternal(in);
            return ackMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void logout() {
        try {
            LoginMessage message = new LoginMessage();
            message.setName("test1");
            message.writeExternal(out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public DataMessage readMessage() {
        try {

            int messageType = in.read();
            assertEquals(messageType, DATA);
            DataMessage dataMessage = new DataMessage();
            dataMessage.readExternal(in);
            return dataMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
