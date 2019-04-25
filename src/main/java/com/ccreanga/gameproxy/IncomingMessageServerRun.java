package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.incoming.IncomingMessageServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ccreanga")
public class IncomingMessageServerRun implements CommandLineRunner {

    @Autowired
    private IncomingMessageServer incomingMessageServer;

    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(incomingMessageServer);
        thread.start();
        Thread.currentThread().join();}

    public static void main(String[] args) {
        SpringApplication.run(IncomingMessageServerRun.class, args);
    }
}
