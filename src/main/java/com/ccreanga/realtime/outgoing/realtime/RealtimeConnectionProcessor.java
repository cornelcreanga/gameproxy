package com.ccreanga.realtime.outgoing.realtime;

import com.ccreanga.protocol.AuthorizationException;
import com.ccreanga.protocol.MalformedException;
import com.ccreanga.protocol.outgoing.MessageIO;
import com.ccreanga.protocol.outgoing.client.*;
import com.ccreanga.realtime.Customer;
import com.ccreanga.realtime.outgoing.handlers.LoginHandler;
import com.ccreanga.realtime.outgoing.handlers.LogoutHandler;
import com.google.common.util.concurrent.Striped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;

import static com.ccreanga.protocol.outgoing.client.ClientMsg.LOGIN;
import static com.ccreanga.protocol.outgoing.client.ClientMsg.LOGOUT;

@Component
@Slf4j
@AllArgsConstructor
public class RealtimeConnectionProcessor {

    private static Striped<ReadWriteLock> stripedLock = Striped.lazyWeakReadWriteLock(100);

    private LoginHandler loginHandler;
    private LogoutHandler logoutHandler;


    public void handleConnection(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        Customer customer = null;
        //add it to register section
        while (true) {
            Optional<ClientMsg> optional = MessageIO.deSerializeClientMsg(in);
            if (optional.isEmpty()) {
                return;
            }
            ClientMsg msg = optional.get();
            log.trace("message type {}", msg.getType());
            switch (msg.getType()) {
                case LOGIN: {
                    Optional<Customer> optionalCustomer = loginHandler.handle(socket, (LoginMsg) msg);
                    if (optionalCustomer.isPresent())
                        customer = optionalCustomer.get();
                    break;
                }
                case -1:log.warn("socket close for customer {}",customer);
                case LOGOUT: {
                    logoutHandler.handle(socket, customer, (LogoutMsg) msg);
                    break;
                }
                default: {
                    //todo - write something back on the socket
                    throw new MalformedException("invalid message type " + msg.getType(), "BAD_MESSAGE_TYPE");
                }
            }

        }
    }

}
