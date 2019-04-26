package com.ccreanga.gameproxy.outgoing;

import static com.ccreanga.gameproxy.outgoing.message.client.ClientMessage.CLIENT_LOGIN;
import static com.ccreanga.gameproxy.outgoing.message.client.ClientMessage.CLIENT_LOGOUT;
import static com.ccreanga.gameproxy.outgoing.message.client.ClientMessage.CLIENT_SEND_DATA;
import static com.ccreanga.gameproxy.outgoing.message.server.LoginResultMessage.ALREADY_AUTHENTICATED;
import static com.ccreanga.gameproxy.outgoing.message.server.LoginResultMessage.AUTHORIZED;
import static com.ccreanga.gameproxy.outgoing.message.server.LoginResultMessage.UNAUTHORIZED;

import com.ccreanga.gameproxy.CurrentSession;
import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.CustomerSessionStatus;
import com.ccreanga.gameproxy.gateway.CustomerStorage;
import com.ccreanga.gameproxy.outgoing.message.client.LoginMessage;
import com.ccreanga.gameproxy.outgoing.message.client.LogoutMessage;
import com.ccreanga.gameproxy.outgoing.message.client.MalformedMessageException;
import com.ccreanga.gameproxy.outgoing.message.client.SendData;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
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
    private CurrentSession currentSession;

    public void handleConnection(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        Customer customer = null;
        //add it to register section
        while(true){
            int a = in.read();
            switch (a){
                case CLIENT_LOGIN:{
                    LoginMessage message = new LoginMessage();
                    message.readExternal(in);
                    LoginResultMessage resultMessage = null;

                    log.info("ClientLoginMessage {}",message.getName());
                    Set<Customer> customers = customerStorage.getCustomers();
                    Optional<Customer> optional = customers.stream().filter(c -> c.getName().equals(message.getName())).findAny();
                    if (!optional.isPresent()) {
                        log.info("Not authorized");
                        resultMessage = new LoginResultMessage(UNAUTHORIZED);
                        resultMessage.writeExternal(out);
                        return;
                    }
                    customer = optional.get();
                    CustomerSessionStatus status = currentSession.login(customer, socket);
                    if (status.isAlreadyLoggedIn()) {
                        resultMessage = new LoginResultMessage(ALREADY_AUTHENTICATED);
                        log.info("Already authorized.");
                    }else{
                        log.info("Authorized");
                        resultMessage = new LoginResultMessage(AUTHORIZED);
                    }

                    resultMessage.writeExternal(out);
                    break;

                }
                case CLIENT_LOGOUT:{
                    if (customer == null) {//ignore
                        log.info("no customer logged in, can't logout");
                        break;
                    } else {
                        LogoutMessage message = new LogoutMessage();
                        message.readExternal(in);
                        currentSession.logout(customer);
                        break;
                    }
                }
                case CLIENT_SEND_DATA:{
                    if (customer == null) {
                        log.info("no customer logged in, can't send data");
                        return;
                    } else {
                        SendData message = new SendData();
                        message.readExternal(in);
                        if (message.getLastTimestamp() > 0) {
                            //todo - read the data from kafka
                        } else {
                            //do nothing, the data is already sent after the login message
                        }
                        break;
                    }
                }
                //socket close
                case -1: {
                    return;
                }
                default:{
                    throw new MalformedMessageException("invalid message type " + a, "BAD_MESSAGE_TYPE");
                }
            }
            out.flush();

        }
    }

}
