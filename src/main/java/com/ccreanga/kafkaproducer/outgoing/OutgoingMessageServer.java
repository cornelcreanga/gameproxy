package com.ccreanga.kafkaproducer.outgoing;

import com.ccreanga.kafkaproducer.IOUtil;
import com.ccreanga.kafkaproducer.incoming.IncomingMessage;
import com.ccreanga.kafkaproducer.ServerConfig;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OutgoingMessageServer implements Runnable {

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private CustomerConnectionManager customerConnectionManager;

    @Autowired
    OutgoingConnectionProcessor outgoingConnectionProcessor;

    private ServerSocket serverSocket = null;
    private boolean isStopped = false;


    public void run() {
        ExecutorService threadPool = new ThreadPoolExecutor(
            64,
            128,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(128));


        try {
            serverSocket = new ServerSocket(serverConfig.getOutgoingMessagePort());
            while (!isStopped) {
                Socket socket = serverSocket.accept();
                socket.setTcpNoDelay(true);
                InputStream input = socket.getInputStream();

                try {
                    threadPool.execute(() -> {
                        try {
                            //keep the connection open unless close/not authorized
                            outgoingConnectionProcessor.handleConnection(socket);
                            IOUtil.closeSocketPreventingReset(socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            IOUtil.closeSocketPreventingReset(socket);
                        }
                    });

                } catch (RejectedExecutionException e) {
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
