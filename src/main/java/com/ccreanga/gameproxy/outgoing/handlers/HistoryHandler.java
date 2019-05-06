package com.ccreanga.gameproxy.outgoing.handlers;

import com.ccreanga.gameproxy.kafka.KafkaMessageConsumer;
import com.ccreanga.gameproxy.outgoing.message.client.HistoryDataMsg;
import com.ccreanga.gameproxy.util.IOUtil;
import java.io.IOException;
import java.net.Socket;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class HistoryHandler {

    private KafkaMessageConsumer consumer;

    public void handle(String topic, Socket socket, HistoryDataMsg message)  throws IOException{
        log.info("ClientHistoryMessage {} {} {}", topic,message.getStartTimestamp(),message.getEndTimestamp());
        consumer.consume(topic,message.getStartTimestamp(),message.getEndTimestamp(),socket.getOutputStream());

        IOUtil.closeSocketPreventingReset(socket);

    }

}
