package com.ccreanga.it;

import com.ccreanga.gameproxy.outgoing.message.MessageIO;
import com.ccreanga.gameproxy.outgoing.message.client.ClientMsg;
import com.ccreanga.gameproxy.outgoing.message.client.LoginMsg;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

public class Cucu {

    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LoginMsg l1 = new LoginMsg("test1");
        MessageIO.serializeClientMsg(l1,baos);

        Optional<ClientMsg> l2 = MessageIO.deSerializeClientMsg(new ByteArrayInputStream(baos.toByteArray()));

        System.out.println(l2);

    }

}
