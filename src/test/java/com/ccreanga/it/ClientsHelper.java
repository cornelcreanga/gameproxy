package com.ccreanga.it;

import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.server.DataEndMsg;
import com.ccreanga.gameproxy.outgoing.message.server.DataMsg;
import com.ccreanga.gameproxy.outgoing.message.server.InfoMsg;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

public class ClientsHelper {

    public static void login(ForkJoinPool threadPool, List<Client> clients){
        CountDownLatch latch = new CountDownLatch(clients.size());

        threadPool.submit(
            () -> clients.parallelStream().forEach(client -> {
                InfoMsg message = client.login();
                assertEquals(message.getCode(), InfoMsg.AUTHORIZED);
                latch.countDown();
            }));

        try {
            latch.await();
        } catch (InterruptedException e) { }
    }


    public static void askForHistory(ForkJoinPool threadPool, List<Client> clients,long startTimestamp,long endTimestamp){
        CountDownLatch latch = new CountDownLatch(clients.size());

        threadPool.submit(
            () -> clients.parallelStream().forEach(client -> {
                client.askForHistory(startTimestamp,endTimestamp);
                latch.countDown();
            }));

        try {
            latch.await();
        } catch (InterruptedException e) { }
    }

    public static Multimap<Client,DataMsg> readHistoryMessages(ForkJoinPool threadPool, List<Client> clients){
        Multimap<Client, DataMsg> values = HashMultimap.create();

        threadPool.submit(
            () -> clients.parallelStream().forEach(client -> {

                while(true){
                    Optional<ServerMsg> dataMsg = client.readMessage();
                    if (dataMsg.isEmpty())
                        break;
                    ServerMsg message = dataMsg.get();
                    if (message instanceof DataEndMsg)
                        break;
                    values.put(client,(DataMsg) message);
                }

            }));
        return values;
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
