package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.outgoing.OutgoingMessageServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ccreanga")
public class OutgoingMessageServerRun implements CommandLineRunner {

    @Autowired
    private OutgoingMessageServer outgoingMessageServer;

    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(outgoingMessageServer);
        thread.start();
        Thread.currentThread().join();

    }

    public static void main(String[] args) {
        SpringApplication.run(OutgoingMessageServerRun.class, args);
    }
}
