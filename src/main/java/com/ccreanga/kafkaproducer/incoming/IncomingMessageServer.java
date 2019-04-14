package com.ccreanga.kafkaproducer.incoming;

import com.ccreanga.kafkaproducer.outgoing.CustomerConnectionManager;
import com.ccreanga.kafkaproducer.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class IncomingMessageServer implements Runnable {

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private CustomerConnectionManager customerConnectionManager;


    private ServerSocket serverSocket = null;
    private boolean isStopped = false;


    public void run() {
        try {
            serverSocket = new ServerSocket(serverConfig.getIncomingMessagePort());
            while (!isStopped) {
                Socket clientSocket = serverSocket.accept();
                InputStream input = clientSocket.getInputStream();
                IncomingMessage message = IncomingMessage.readExternal(input);
                customerConnectionManager.sendMessageToCustomers(message);
                //List<String> topicManager.getTopics(message.getMatchId());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Server Stopped.");
    }

    public synchronized void stop() {
        isStopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }


}
