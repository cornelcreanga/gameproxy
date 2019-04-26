package com.ccreanga.it;

import static com.ccreanga.gameproxy.outgoing.message.server.ServerMessage.DATA;
import static com.ccreanga.gameproxy.outgoing.message.server.ServerMessage.LOGIN_RESULT;
import static org.junit.Assert.assertEquals;

import com.ccreanga.gameproxy.incoming.IncomingMessage;
import com.ccreanga.gameproxy.outgoing.message.client.LoginMessage;
import com.ccreanga.gameproxy.outgoing.message.server.DataMessage;
import com.ccreanga.gameproxy.outgoing.message.server.LoginResultMessage;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.junit.Test;

public class FlowTest {

    @Test
    public void testBasicFlow() throws Exception {
        Socket socketConsumer = new Socket("127.0.0.1", 8082);
        socketConsumer.setSoTimeout(5000);
        InputStream inConsumer = socketConsumer.getInputStream();
        OutputStream outConsumer = socketConsumer.getOutputStream();

        LoginMessage message = new LoginMessage();
        message.setName("test1");
        message.writeExternal(outConsumer);

        int messageType = inConsumer.read();
        LoginResultMessage ackMessage = new LoginResultMessage();
        assertEquals(messageType, LOGIN_RESULT);
        ackMessage.readExternal(inConsumer);
        assertEquals(ackMessage.getResult(), LoginResultMessage.AUTHORIZED);

        Socket socketProducer = new Socket("127.0.0.1", 8081);
        socketProducer.setSoTimeout(5000);
        IncomingMessage incomingMessage = new IncomingMessage(1, 2L, "some message".getBytes(), System.currentTimeMillis());
        incomingMessage.writeExternal(socketProducer.getOutputStream());

        Thread.sleep(1000);

        DataMessage dataMessage = new DataMessage();
        messageType = inConsumer.read();
        assertEquals(messageType, DATA);
        dataMessage.readExternal(inConsumer);

        assertEquals(dataMessage.getMessage().getId(), 1);

    }
}
