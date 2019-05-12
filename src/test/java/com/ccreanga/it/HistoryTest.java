package com.ccreanga.it;

import com.ccreanga.protocol.outgoing.server.DataMsg;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import org.junit.Test;

public class HistoryTest {

    @Test
    public void testHistoryFlow() throws Exception {
        Producer producer = new Producer("127.0.0.1", 8081);


        UUID uuid1 = UUID.randomUUID();
        long timestamp1 = System.currentTimeMillis();
        producer.produce(uuid1, 1L, "some message1",timestamp1);
        Thread.sleep(1000);// 1 sec should be enough
        UUID uuid2 = UUID.randomUUID();
        long timestamp2 = System.currentTimeMillis();
        producer.produce(uuid2, 1L, "some message2",timestamp2);

        Thread.sleep(1000);
        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Client client = new Client("test" + i, "127.0.0.1", 8083);
            clients.add(client);
        }
        ForkJoinPool threadPool = new ForkJoinPool(16);

        ClientsHelper.login(threadPool,clients);

        ClientsHelper.askForHistory(threadPool,clients,timestamp2,timestamp2+1);

        Multimap<Client, DataMsg> values = ClientsHelper.readHistoryMessages(threadPool,clients);

        ClientsHelper.logout(threadPool,clients);

    }

}
