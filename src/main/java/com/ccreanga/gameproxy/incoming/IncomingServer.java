package com.ccreanga.gameproxy.incoming;

import com.ccreanga.gameproxy.ServerConfig;
import com.ccreanga.gameproxy.util.IOUtil;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IncomingServer implements Runnable {

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    IncomingConnectionProcessor incomingConnectionProcessor;

    private ServerSocket serverSocket = null;
    private boolean isStopped = false;

    public void run() {

        try {
            serverSocket = new ServerSocket(serverConfig.getIncomingPort());
            log.info("outgoing server started on {}", serverConfig.getIncomingPort());
            while (!isStopped) {
                Socket socket = serverSocket.accept();
                socket.setTcpNoDelay(true);

                try {
                    //keep the connection open unless close/not authorized
                    //single threaded for the moment, to be redesigned if message processing will be time expensive
                    incomingConnectionProcessor.handleConnection(socket);
                } catch (IOException e) {
                    if (!e.getMessage().equals("Connection reset")) {
                        e.printStackTrace();
                    }
                } finally {
                    IOUtil.closeSocketPreventingReset(socket);
                }


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
