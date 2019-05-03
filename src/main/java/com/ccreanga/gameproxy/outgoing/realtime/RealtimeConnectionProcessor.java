package com.ccreanga.gameproxy.outgoing.realtime;

import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsg.CLIENT_LOGIN;
import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsg.CLIENT_LOGOUT;
import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsgFactory.loginMsg;
import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsgFactory.logoutMsg;
import static com.ccreanga.gameproxy.outgoing.message.server.LoginResultMsg.ALREADY_AUTHENTICATED;
import static com.ccreanga.gameproxy.outgoing.message.server.LoginResultMsg.AUTHORIZED;
import static com.ccreanga.gameproxy.outgoing.message.server.LoginResultMsg.UNAUTHORIZED;

import com.ccreanga.gameproxy.CurrentSession;
import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.CustomerSessionStatus;
import com.ccreanga.gameproxy.gateway.CustomerStorage;
import com.ccreanga.gameproxy.outgoing.message.client.LoginMsg;
import com.ccreanga.gameproxy.outgoing.message.client.LogoutMsg;
import com.ccreanga.gameproxy.outgoing.message.client.MalformedException;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMsg;
import com.google.common.util.concurrent.Striped;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
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
                case CLIENT_LOGIN: {
                    LoginMsg message = loginMsg(in);

                    LoginResultMsg resultMessage;
                    String name = message.getName();
                    log.info("ClientLoginMessage {}", name);

                    Lock customerLock = stripedLock.get(name).readLock();//prevent concurrent login for the same customers
                    customerLock.lock();

                    try {
                        Set<Customer> customers = customerStorage.getCustomers();
                        Optional<Customer> optional = customers.stream().filter(c -> c.getName().equals(name)).findAny();
                        if (optional.isEmpty()) {
                            log.info("Not authorized");
                            resultMessage = new LoginResultMsg(UNAUTHORIZED);
                            resultMessage.writeExternal(out);
                            return;
                        }
                        customer = optional.get();
                        CustomerSessionStatus status = currentSession.login(customer, socket);
                        if (status.isAlreadyLoggedIn()) {
                            resultMessage = new LoginResultMsg(ALREADY_AUTHENTICATED);
                            log.info("Already authorized.");
                        } else {
                            log.info("Authorized");
                            resultMessage = new LoginResultMsg(AUTHORIZED);
                        }

                        resultMessage.writeExternal(out);
                        break;
                    } finally {
                        customerLock.unlock();
                    }

                }
                case CLIENT_LOGOUT: {
                    if (customer == null) {//ignore
                        log.info("no customer logged in, can't logout");
                        break;
                    } else {
                        LogoutMsg message = logoutMsg(in);
                        currentSession.logout(customer);
                        break;
                    }
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
