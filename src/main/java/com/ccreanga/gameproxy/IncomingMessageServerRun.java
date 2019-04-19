package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.incoming.IncomingMessageServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IncomingMessageServerRun implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(new IncomingMessageServer());
        thread.start();
        Thread.currentThread().join();}

    public static void main(String[] args) {
        SpringApplication.run(IncomingMessageServerRun.class, args);
    }
}
