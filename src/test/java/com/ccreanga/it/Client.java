package com.ccreanga.it;

import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.client.HistoryDataMsg;
import com.ccreanga.gameproxy.outgoing.message.client.LoginMsg;
import com.ccreanga.gameproxy.outgoing.message.client.LogoutMsg;
import com.ccreanga.gameproxy.outgoing.message.MessageIO;
import com.ccreanga.gameproxy.outgoing.message.server.DataMsg;
import com.ccreanga.gameproxy.outgoing.message.server.InfoMsg;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

public class Client {

    private String name;
    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private Thread mainThread;


    public Client(String name, String ip, int port) {
        this.name = name;
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(2000);
            out = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public InfoMsg login() {
        try {
            MessageIO.serializeClientMsg(new LoginMsg(name),out);
            InfoMsg infoMsg = (InfoMsg)MessageIO.deSerializeServerMsg(in).get();
            return infoMsg;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void askForHistory(long startTimestamp, long endTimestamp){
        try {
            HistoryDataMsg message = new HistoryDataMsg(startTimestamp,endTimestamp);
            MessageIO.serializeClientMsg(message,out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void logout() {
        try {
            MessageIO.serializeClientMsg(new LogoutMsg(),out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ServerMsg> readMessage() {
        try {
            return MessageIO.deSerializeServerMsg(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DataMsg readDataMessage(){
        Optional<ServerMsg> optional = null;
        try {
            optional = MessageIO.deSerializeServerMsg(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (optional.isEmpty())
            throw new RuntimeException("end of data reached");
        ServerMsg message = optional.get();
        if (message instanceof DataMsg)
            return (DataMsg)message;
        throw new RuntimeException("expected DataMsg, received "+message.getClass());
    }


}
