package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.incoming.IncomingServer;
import com.ccreanga.gameproxy.outgoing.history.HistoryServer;
import com.ccreanga.gameproxy.outgoing.realtime.RealtimeServer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private HistoryServer historyServer;

    @Override
    public void run(String... args) throws Exception {
        Thread thread1 = new Thread(incomingServer);
        thread1.start();
        Thread thread2 = new Thread(realtimeServer);
        thread2.start();
        Thread thread3 = new Thread(historyServer);
        thread3.start();


    }

    public static void main(String[] args) {
        SpringApplication.run(ServerRun.class, args);
    }
}
