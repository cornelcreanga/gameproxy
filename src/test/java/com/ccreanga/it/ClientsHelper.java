package com.ccreanga.it;

import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMsg;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

public class ClientsHelper {

    public static void login(ForkJoinPool threadPool, List<Client> clients){
        CountDownLatch latch = new CountDownLatch(clients.size());

        threadPool.submit(
            () -> clients.parallelStream().forEach(client -> {
                LoginResultMsg message = client.login();

                assertEquals(message.getResult(), LoginResultMsg.AUTHORIZED);
                latch.countDown();
            }));

        try {
            latch.await();
        } catch (InterruptedException e) { }
    }


    public static void logout(ForkJoinPool threadPool, List<Client> clients){
        CountDownLatch latch = new CountDownLatch(clients.size());

        threadPool.submit(
            () -> clients.parallelStream().forEach(client -> {
                client.logout();
                latch.countDown();
            }));

        try {
            latch.await();
        } catch (InterruptedException e) { }
    }

}
