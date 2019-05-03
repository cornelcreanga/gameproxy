package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.incoming.IncomingServer;
import com.ccreanga.gameproxy.outgoing.realtime.RealtimeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ccreanga")
public class ServerRun implements CommandLineRunner {

    @Autowired
    private IncomingServer incomingServer;

    @Autowired
    private RealtimeServer realtimeServer;

    @Override
    public void run(String... args) throws Exception {
        Thread thread1 = new Thread(incomingServer);
        thread1.start();
        Thread thread2 = new Thread(realtimeServer);
        thread2.start();

    }

    public static void main(String[] args) {
        SpringApplication.run(ServerRun.class, args);
    }
}
