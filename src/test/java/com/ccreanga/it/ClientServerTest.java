package com.ccreanga.it;

import com.ccreanga.kafkaproducer.outgoing.message.ClientLoginMessage;
import com.ccreanga.kafkaproducer.outgoing.message.ServerLoginResultMessage;
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
        new ClientLoginMessage("test1").writeExternal(socket.getOutputStream());
        ServerLoginResultMessage ackMessage = ServerLoginResultMessage.readExternal(socket.getInputStream());
        assertEquals(ackMessage.getType(), ServerLoginResultMessage.AUTHORIZED);
    }

    @Test
    public void testNotAuthorized() throws IOException {
        Socket socket = new Socket("localhost", 8081);
        socket.setSoTimeout(5000);
        new ClientLoginMessage("test-unknown").writeExternal(socket.getOutputStream());
        ServerLoginResultMessage ackMessage = ServerLoginResultMessage.readExternal(socket.getInputStream());
        assertEquals(ackMessage.getType(), ServerLoginResultMessage.UNAUTHORIZED);
    }


}
