package com.ccreanga.gameproxy.outgoing.handlers;

import static com.ccreanga.gameproxy.outgoing.message.client.ClientMsgFactory.logoutMsg;

import com.ccreanga.gameproxy.CurrentSession;
import com.ccreanga.gameproxy.Customer;
import com.ccreanga.gameproxy.gateway.CustomerStorage;
import com.ccreanga.gameproxy.outgoing.message.client.LogoutMsg;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LogoutHandler {

    private CurrentSession currentSession;


    public void handle(Socket socket,Customer customer) throws IOException {

        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        LogoutMsg message = logoutMsg(in);
        if (customer==null) {//ignore
            log.info("no customer logged in, can't logout");
        } else {
            currentSession.logout(customer);
        }
    }

}
