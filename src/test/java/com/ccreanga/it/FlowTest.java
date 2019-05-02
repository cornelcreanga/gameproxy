package com.ccreanga.it;

import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.server.DataMsg;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMsg;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import org.junit.Test;

public class FlowTest {

    @Test
    public void testBasicFlow() throws Exception {

        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Client client = new Client("test" + i, "127.0.0.1", 8082);
            clients.add(client);
        }
        ForkJoinPool threadPool = new ForkJoinPool(16);
        CountDownLatch latchLogin = new CountDownLatch(clients.size());

        threadPool.submit(
            () -> clients.parallelStream().forEach(client -> {
                LoginResultMsg message = client.login();
                assertEquals(message.getResult(), LoginResultMsg.AUTHORIZED);
                latchLogin.countDown();
            }));

        latchLogin.await();

        Producer producer = new Producer("127.0.0.1", 8081);
        UUID uuid = UUID.randomUUID();
        producer.produce(uuid, 1L, "some message");
        CountDownLatch latchConsume = new CountDownLatch(clients.size());
        threadPool.submit(
            () -> clients.parallelStream().forEach(client -> {
                DataMsg message = client.readMessage();
                assertEquals(message.getMessage().getId(), uuid);
                latchConsume.countDown();
            }));

        latchConsume.await();

        CountDownLatch latchLogout = new CountDownLatch(clients.size());
        threadPool.submit(
            () -> clients.parallelStream().forEach(client -> {
                client.logout();
                latchLogout.countDown();
            }));
        latchLogout.await();

    }

}
