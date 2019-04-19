package com.ccreanga.it;

import com.ccreanga.gameproxy.outgoing.message.client.LoginMessage;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMessage;
import java.io.IOException;
import java.net.Socket;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ClientServerTest {

    @Test
    public void testAuthorized() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8081);
        socket.setSoTimeout(5000);
        new LoginMessage("test1").writeExternal(socket.getOutputStream());
        LoginResultMessage ackMessage = LoginResultMessage.readExternal(socket.getInputStream());
        assertEquals(ackMessage.getResult(), LoginResultMessage.AUTHORIZED);
    }

    @Test
    public void testNotAuthorized() throws IOException {
        Socket socket = new Socket("localhost", 8081);
        socket.setSoTimeout(5000);
        new LoginMessage("test-unknown").writeExternal(socket.getOutputStream());
        LoginResultMessage ackMessage = LoginResultMessage.readExternal(socket.getInputStream());
        assertEquals(ackMessage.getResult(), LoginResultMessage.UNAUTHORIZED);
    }


}
