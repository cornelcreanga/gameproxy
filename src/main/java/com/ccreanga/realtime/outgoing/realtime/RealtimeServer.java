package com.ccreanga.realtime.outgoing.realtime;

import com.ccreanga.realtime.ServerConfig;
import com.ccreanga.realtime.util.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Component
@Slf4j
public class RealtimeServer implements Runnable {

    private ServerConfig serverConfig;

    private RealtimeConnectionProcessor outgoingConnectionProcessor;

    private ServerSocket serverSocket = null;
    private boolean isStopped = false;

    public RealtimeServer(ServerConfig serverConfig, RealtimeConnectionProcessor outgoingConnectionProcessor) {
        this.serverConfig = serverConfig;
        this.outgoingConnectionProcessor = outgoingConnectionProcessor;
    }


    public void run() {
        ExecutorService threadPool = new ThreadPoolExecutor(
                64,
                128,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(128));

        try {
            serverSocket = new ServerSocket(serverConfig.getRealtimePort());
            log.info("outgoing server started on {}", serverConfig.getRealtimePort());
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
                        } catch (Exception e) {
                            if (!e.getMessage().equals("Connection reset")) {
                                e.printStackTrace();
                            }
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
