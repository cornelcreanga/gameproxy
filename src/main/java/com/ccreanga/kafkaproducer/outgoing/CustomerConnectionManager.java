package com.ccreanga.kafkaproducer.outgoing;

import com.ccreanga.kafkaproducer.Customer;
import com.ccreanga.kafkaproducer.gateway.CustomerStorage;
import com.ccreanga.kafkaproducer.incoming.IncomingMessage;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.Socket;
import java.util.*;

@Service
public class CustomerConnectionManager {

    @Autowired
    private CustomerStorage customerStorage;

    private ListMultimap<Long, Socket> socketMap = ArrayListMultimap.create();

    private Map<Socket,Long> socketLastSend = new HashMap<>();

    public void sendMessageToCustomers(IncomingMessage message) throws IOException {
        List<Socket> sockets = socketMap.get(message.getMatchId());
        for (Socket next : sockets) {
            message.writeExternal(next.getOutputStream());
            next.getOutputStream().flush();
        }
    }

    public void registerCustomerConnection(Customer customer,Socket socket){
        List<Long> matches = customer.getMatches();
        for (Long matchId : matches) {
            socketMap.put(matchId, socket);
        }

    }


}
