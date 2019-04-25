package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.incoming.IncomingMessageServer;
import com.ccreanga.gameproxy.outgoing.OutgoingMessageServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ccreanga")
public class ServerRun implements CommandLineRunner {

    @Autowired
    private IncomingMessageServer incomingMessageServer;

    @Autowired
    private OutgoingMessageServer outgoingMessageServer;

    @Override
    public void run(String... args) throws Exception {
        Thread thread1 = new Thread(incomingMessageServer);
        thread1.start();
        Thread thread2 = new Thread(outgoingMessageServer);
        thread2.start();

    }

    public static void main(String[] args) {
        SpringApplication.run(ServerRun.class, args);
    }
}
