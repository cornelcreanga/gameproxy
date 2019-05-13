package com.ccreanga.realtime.outgoing.handlers;

import com.ccreanga.protocol.outgoing.client.LogoutMsg;
import com.ccreanga.realtime.CurrentSession;
import com.ccreanga.realtime.Customer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;

@Slf4j
@Component
@AllArgsConstructor
public class LogoutHandler {

    private CurrentSession currentSession;


    public void handle(Socket socket, Customer customer, LogoutMsg message) throws IOException {

        if (customer == null) {//ignore
            log.warn("no customer logged in for this connection, can't logout");
        } else {
            currentSession.logout(customer);
        }
    }

}
