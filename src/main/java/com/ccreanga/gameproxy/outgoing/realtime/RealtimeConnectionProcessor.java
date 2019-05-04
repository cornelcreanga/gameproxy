package com.ccreanga.gameproxy.outgoing.realtime;

import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsg.LOGIN;
import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsg.LOGOUT;

import com.ccreanga.gameproxy.CurrentSession;
import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.gateway.CustomerStorage;
import com.ccreanga.gameproxy.outgoing.handlers.LoginHandler;
import com.ccreanga.gameproxy.outgoing.handlers.LogoutHandler;
import com.ccreanga.gameproxy.outgoing.message.client.AuthorizationException;
import com.ccreanga.gameproxy.outgoing.message.client.MalformedException;
import com.google.common.util.concurrent.Striped;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RealtimeConnectionProcessor {

    Striped<ReadWriteLock> stripedLock = Striped.lazyWeakReadWriteLock(100);

    @Autowired
    private CustomerStorage customerStorage;

    @Autowired
    private CurrentSession currentSession;

    public void handleConnection(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        Customer customer = null;
        //add it to register section
        while (true) {
            int a = in.read();
            switch (a) {
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
                //socket close
                case -1: {
                    return;
                }
                default: {
                    //todo - write something back on the socket
                    throw new MalformedException("invalid message type " + a, "BAD_MESSAGE_TYPE");
                }
            }
            out.flush();

        }
    }

}
