package com.ccreanga.kafkaproducer.outgoing;

import static com.ccreanga.kafkaproducer.outgoing.message.client.AbstractMessage.CLIENT_LOGIN;
import static com.ccreanga.kafkaproducer.outgoing.message.client.AbstractMessage.CLIENT_LOGOUT;
import static com.ccreanga.kafkaproducer.outgoing.message.client.AbstractMessage.CLIENT_SEND_DATA;

import com.ccreanga.kafkaproducer.Customer;
import com.ccreanga.kafkaproducer.gateway.CustomerStorage;
import com.ccreanga.kafkaproducer.outgoing.message.client.LogoutMessage;
import com.ccreanga.kafkaproducer.outgoing.message.client.SendData;
import com.ccreanga.kafkaproducer.outgoing.message.client.LoginMessage;
import com.ccreanga.kafkaproducer.outgoing.message.server.LoginResultMessage;
import java.io.IOException;
import java.io.InputStream;
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
        InputStream in = socket.getInputStream();
        Customer customer = null;
        //add it to register section
        out.flush();
        while(true){
            int a = in.read();
            switch (a){
                case CLIENT_LOGIN:{
                    LoginMessage message = LoginMessage.readExternal(in);
                    //todo - already login?
                    log.info("ClientLoginMessage {}",message.getName());
                    Set<Customer> customers = customerStorage.getCustomers();

                    for (Customer next : customers) {
                        if (next.getName().equals(message.getName())) {
                            customer = next;
                            break;
                        }

                    }

                    if (customer==null){
                        log.info("Not authorized");
                        new LoginResultMessage(LoginResultMessage.UNAUTHORIZED).writeExternal(out);
                        out.flush();
                        return;
                    }

                    //todo -add to session
                    log.info("Authorized");
                    new LoginResultMessage(LoginResultMessage.AUTHORIZED).writeExternal(out);

                }
                case CLIENT_LOGOUT:{
                    LogoutMessage message = LogoutMessage.readExternal(in);
                    customerConnectionManager.unregisterCustomerConnection(socket);
                    //todo - remove from session
                }
                case CLIENT_SEND_DATA:{
                    SendData message = SendData.readExternal(in);
                    if (message.getSecondsInterval()>0){
                        //kafka
                    }else{

                    }
                }
                default:{

                }
            }

        }
    }

}
