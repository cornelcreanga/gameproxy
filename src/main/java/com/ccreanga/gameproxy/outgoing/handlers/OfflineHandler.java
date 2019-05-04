package com.ccreanga.gameproxy.outgoing.handlers;

import com.ccreanga.gameproxy.kafka.KafkaMessageConsumer;
import com.ccreanga.gameproxy.util.IOUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import org.springframework.beans.factory.annotation.Autowired;

public class OfflineHandler {

    @Autowired
    private KafkaMessageConsumer consumer;

    public void handle(String topic,long startTimestamp,long endTimestamp, Socket socket)  throws IOException{

        consumer.consume(topic,startTimestamp,endTimestamp,socket.getOutputStream());

        IOUtil.closeSocketPreventingReset(socket);

    }

}
