package com.ccreanga.it;

import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.client.LoginMsg;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMsg;
import java.io.IOException;
import java.net.Socket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ConsumerTest {

    @Test
    public void testAuthorized() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8082);
        socket.setSoTimeout(5000);
        LoginMsg message = new LoginMsg();
        message.setName("test1");
        message.writeExternal(socket.getOutputStream());
        LoginResultMsg ackMessage = new LoginResultMsg();
        ackMessage.readExternal(socket.getInputStream());
        assertEquals(ackMessage.getResult(), LoginResultMsg.AUTHORIZED);
    }

    @Test
    public void testNotAuthorized() throws IOException {
        Socket socket = new Socket("localhost", 8082);
        socket.setSoTimeout(5000);
        LoginMsg message = new LoginMsg();
        message.setName("test-unknown");
        message.writeExternal(socket.getOutputStream());

        LoginResultMsg ackMessage = new LoginResultMsg();
        ackMessage.readExternal(socket.getInputStream());

        assertEquals(ackMessage.getResult(), LoginResultMsg.UNAUTHORIZED);
    }


}
