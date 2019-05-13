package com.ccreanga.realtime.incoming;

import com.ccreanga.realtime.ServerConfig;
import com.ccreanga.realtime.util.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@Slf4j
public class IncomingServer implements Runnable {

    private ServerConfig serverConfig;
    private IncomingConnectionProcessor incomingConnectionProcessor;

    private ServerSocket serverSocket = null;
    private boolean isStopped = false;

    public IncomingServer(ServerConfig serverConfig, IncomingConnectionProcessor incomingConnectionProcessor) {
        this.serverConfig = serverConfig;
        this.incomingConnectionProcessor = incomingConnectionProcessor;
    }

    public void run() {

        try {
            serverSocket = new ServerSocket(serverConfig.getIncomingPort());
            log.info("incoming server started on {}", serverConfig.getIncomingPort());
            while (!isStopped) {
                Socket socket = serverSocket.accept();
                socket.setTcpNoDelay(true);

                try {
                    //keep the connection open unless close/not authorized
                    //single threaded for the moment, to be redesigned if message processing will be time expensive
                    incomingConnectionProcessor.handleConnection(socket);
                } catch (IOException e) {
                    String message = e.getMessage();
                    log.warn("ioexception",e);//todo

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
