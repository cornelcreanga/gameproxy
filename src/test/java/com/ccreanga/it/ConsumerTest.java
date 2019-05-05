package com.ccreanga.it;

import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.client.LoginMsg;
import com.ccreanga.gameproxy.outgoing.message.client.LogoutMsg;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMsg;
import java.io.IOException;
import java.net.Socket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ConsumerTest {

    @Test
    public void testAuthorization() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8082);
        socket.setSoTimeout(5000);

        new LoginMsg("test1").writeExternal(socket.getOutputStream());

        LoginResultMsg ackMessage = new LoginResultMsg();
        ackMessage.readExternal(socket.getInputStream());
        assertEquals(ackMessage.getResult(), LoginResultMsg.AUTHORIZED);

        new LoginMsg("test1").writeExternal(socket.getOutputStream());

        ackMessage = new LoginResultMsg();
        ackMessage.readExternal(socket.getInputStream());
        assertEquals(ackMessage.getResult(), LoginResultMsg.ALREADY_AUTHENTICATED);

        new LogoutMsg("test1").writeExternal(socket.getOutputStream());
    }

    @Test
    public void testNotAuthorized() throws IOException {
        Socket socket = new Socket("localhost", 8082);
        socket.setSoTimeout(5000);
        new LoginMsg("not-authorized").writeExternal(socket.getOutputStream());
        LoginResultMsg ackMessage = new LoginResultMsg();
        ackMessage.readExternal(socket.getInputStream());

        assertEquals(ackMessage.getResult(), LoginResultMsg.UNAUTHORIZED);
    }


}
