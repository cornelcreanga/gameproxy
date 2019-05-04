package com.ccreanga.gameproxy.outgoing.history;

import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsg.LOGIN;
import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsg.LOGOUT;
import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsg.HISTORICAL_DATA;
import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsgFactory.sendDataMsg;

import com.ccreanga.gameproxy.CurrentSession;
import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.gateway.CustomerStorage;
import com.ccreanga.gameproxy.outgoing.handlers.LoginHandler;
import com.ccreanga.gameproxy.outgoing.handlers.LogoutHandler;
import com.ccreanga.gameproxy.outgoing.message.client.AuthorizationException;
import com.ccreanga.gameproxy.outgoing.message.client.MalformedException;
import com.ccreanga.gameproxy.outgoing.message.client.OfflineDataMsg;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HistoryConnectionProcessor {

    private CustomerStorage customerStorage;

    private CurrentSession currentSession;

    public HistoryConnectionProcessor(CustomerStorage customerStorage, CurrentSession currentSession) {
        this.customerStorage = customerStorage;
        this.currentSession = currentSession;
    }

    public void handleConnection(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        Customer customer = null;
        //add it to register section
        while(true){
            int a = in.read();
            switch (a){
                case LOGIN:{
                    LoginHandler loginHandler = new LoginHandler();
                    Optional<Customer> optional = loginHandler.handle(socket);
                    if (optional.isPresent())
                        customer = optional.get();
                    else{
                        throw new AuthorizationException();//todo
                    }
                    break;
                }
                case LOGOUT:{
                    LogoutHandler logoutHandler = new LogoutHandler();
                    logoutHandler.handle(socket,customer);
                    break;
                }
                case HISTORICAL_DATA:{
                    if (customer == null) {
                        log.info("no customer logged in, can't send data");
                        return;
                    } else {
                        OfflineDataMsg message = sendDataMsg(in);

//                        if (message.getLastTimestamp() > 0) {
//                            //todo - read the data from kafka
//                        } else {
//                            //do nothing, the data is already sent after the login message
//                        }
//                        break;
                    }
                }
                //socket close
                case -1: {
                    return;
                }
                default:{
                    throw new MalformedException("invalid message type " + a, "BAD_MESSAGE_TYPE");
                }
            }
            out.flush();

        }
    }

}
