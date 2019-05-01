package com.ccreanga.it;

import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.server.DataMessage;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
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
        CountDownLatch latch = new CountDownLatch(clients.size());

        threadPool.submit(
            () -> clients.parallelStream().forEach(client -> {
                LoginResultMessage message = client.login();
                assertEquals(message.getResult(), LoginResultMessage.AUTHORIZED);
                latch.countDown();
            }));

        latch.await(10, TimeUnit.SECONDS);

        Producer producer = new Producer("127.0.0.1", 8081);
        producer.produce(1, 1L, "some message");

        threadPool.submit(
            () -> clients.parallelStream().forEach(client -> {
                DataMessage message = client.readMessage();
                assertEquals(message.getMessage().getId(), 1);
            }));


    }

}
