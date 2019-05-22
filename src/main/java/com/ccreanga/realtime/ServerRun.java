package com.ccreanga.realtime;

import com.ccreanga.realtime.incoming.IncomingServer;
import com.ccreanga.realtime.outgoing.realtime.RealtimeServer;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@AllArgsConstructor
@ComponentScan("com.ccreanga")
public class ServerRun implements CommandLineRunner {

    private IncomingServer incomingServer;

    private RealtimeServer realtimeServer;

    public static void main(String[] args) {
        SpringApplication.run(ServerRun.class, args);
    }

    @Override
    public void run(String... args) {
        Thread thread1 = new Thread(incomingServer);
        thread1.start();
        Thread thread2 = new Thread(realtimeServer);
        thread2.start();
    }
}
