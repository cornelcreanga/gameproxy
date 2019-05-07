package com.ccreanga.it;

import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.server.DataMsg;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import org.junit.Test;

public class FlowTest {

    /**
     * send a message and expect that only the interested parties will receive it
     */
    @Test
    public void testBasicFlow() throws Exception {

        Client[] clients = new Client[5];
        for (int i = 0; i < 5; i++) {
            Client client = new Client("test" + (i+1), "127.0.0.1", 8082);
            clients[i] = client;
        }
        ForkJoinPool threadPool = new ForkJoinPool(16);

        ClientsHelper.login(threadPool, Arrays.asList(clients));

        Producer producer = new Producer("127.0.0.1", 8081);
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        producer.produce(uuid1, 2L, "some message");
        producer.produce(uuid2, 1L, "some message");

        DataMsg dataMsg;
        dataMsg = clients[0].readDataMessage();
        dataMsg = clients[1].readDataMessage();
        dataMsg = clients[3].readDataMessage();
        dataMsg = clients[4].readDataMessage();

        dataMsg = clients[0].readDataMessage();
        dataMsg = clients[1].readDataMessage();
        dataMsg = clients[2].readDataMessage();
        dataMsg = clients[3].readDataMessage();
        dataMsg = clients[4].readDataMessage();



        ClientsHelper.logout(threadPool,Arrays.asList(clients));
    }

}
