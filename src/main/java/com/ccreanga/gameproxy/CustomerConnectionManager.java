package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.gateway.CustomerStorage;
import com.ccreanga.gameproxy.incoming.IncomingMessage;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        for (Socket socket : sockets) {
            OutputStream out = socket.getOutputStream();
            message.writeExternal(out);
            out.flush();
        }
    }

//    public void registerCustomerConnection(Customer customer,Socket socket){
//        List<Long> matches = customer.getMatches();
//        for (Long matchId : matches) {
//            socketMap.put(matchId, socket);
//        }
//    }


    public void unregisterCustomerConnection(Socket socket){
        Set<Long> keys = socketMap.keySet();
        keys.forEach(k->{
           List<Socket> sockets = socketMap.get(k);
           sockets.remove(socket);
        });
    }

}
