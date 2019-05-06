package com.ccreanga.it;

import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.outgoing.message.MessageIO;
import com.ccreanga.gameproxy.outgoing.message.client.LoginMsg;
import com.ccreanga.gameproxy.outgoing.message.client.LogoutMsg;
import java.net.Socket;

import com.ccreanga.gameproxy.outgoing.message.server.InfoMsg;
import static com.ccreanga.gameproxy.outgoing.message.server.InfoMsg.*;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ConsumerTest {

    @Test
    public void testAuthorization() throws Exception {
        Socket socket = new Socket("127.0.0.1", 8082);
        socket.setSoTimeout(5000);

        MessageIO.serializeClientMsg(new LoginMsg("test1"),socket.getOutputStream());
        InfoMsg serverMsg = (InfoMsg)MessageIO.deSerializeServerMsg(socket.getInputStream()).get();
        assertEquals(serverMsg.getCode(), AUTHORIZED);

        MessageIO.serializeClientMsg(new LoginMsg("test1"),socket.getOutputStream());
        serverMsg = (InfoMsg)MessageIO.deSerializeServerMsg(socket.getInputStream()).get();
        assertEquals(serverMsg.getCode(), ALREADY_AUTHENTICATED);

        new LogoutMsg("test1").writeExternal(socket.getOutputStream());
    }

    @Test
    public void testNotAuthorized() throws Exception {
        Socket socket = new Socket("localhost", 8082);
        socket.setSoTimeout(5000);

        MessageIO.serializeClientMsg(new LoginMsg("unauthorized"),socket.getOutputStream());
        InfoMsg serverMsg = (InfoMsg)MessageIO.deSerializeServerMsg(socket.getInputStream()).get();
        assertEquals(serverMsg.getCode(), UNAUTHORIZED);
    }


}
