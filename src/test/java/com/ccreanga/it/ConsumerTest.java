package com.ccreanga.it;

import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.client.LoginMessage;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMessage;
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
        LoginMessage message = new LoginMessage();
        message.setName("test1");
        message.writeExternal(socket.getOutputStream());
        LoginResultMessage ackMessage = new LoginResultMessage();
        ackMessage.readExternal(socket.getInputStream());
        assertEquals(ackMessage.getResult(), LoginResultMessage.AUTHORIZED);
    }

    @Test
    public void testNotAuthorized() throws IOException {
        Socket socket = new Socket("localhost", 8082);
        socket.setSoTimeout(5000);
        LoginMessage message = new LoginMessage();
        message.setName("test-unknown");
        message.writeExternal(socket.getOutputStream());

        LoginResultMessage ackMessage = new LoginResultMessage();
        ackMessage.readExternal(socket.getInputStream());

        assertEquals(ackMessage.getResult(), LoginResultMessage.UNAUTHORIZED);
    }


}
