package com.ccreanga.kafkaproducer.outgoing;

import com.ccreanga.kafkaproducer.Customer;
import com.ccreanga.kafkaproducer.gateway.CustomerStorage;
import com.ccreanga.kafkaproducer.outgoing.message.ClientLoginMessage;
import com.ccreanga.kafkaproducer.outgoing.message.ServerLoginResultMessage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OutgoingConnectionProcessor {

    @Autowired
    private CustomerStorage customerStorage;

    @Autowired
    private CustomerConnectionManager customerConnectionManager;

    public void handleConnection(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        ClientLoginMessage message = ClientLoginMessage.readExternal(socket.getInputStream());
        log.info("ClientLoginMessage {}",message.getName());
        Set<Customer> customers = customerStorage.getCustomers();
        Customer customer = null;
        for (Customer next : customers) {
            if (next.getName().equals(message.getName())) {
                customer = next;
                break;
            }

        }
        if (customer!=null){
            log.info("Authorized");
            customerConnectionManager.registerCustomerConnection(customer,socket);
            new ServerLoginResultMessage(ServerLoginResultMessage.AUTHORIZED).writeExternal(out);
        }else{
            log.info("Not authorized");
            new ServerLoginResultMessage(ServerLoginResultMessage.UNAUTHORIZED).writeExternal(out);
        }
        out.flush();
    }

}
