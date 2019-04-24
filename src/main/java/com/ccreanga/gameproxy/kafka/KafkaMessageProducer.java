package com.ccreanga.gameproxy.kafka;

import com.ccreanga.gameproxy.incoming.IncomingMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class KafkaMessageProducer {

    @Autowired
    private KafkaTemplate<Long, byte[]> kafkaTemplate;

    public void sendAsynchToKafka(String topic,IncomingMessage message) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
        message.writeExternal(baos);
        ProducerRecord<Long, byte[]> record = new ProducerRecord<>(topic, message.getMatchId(), baos.toByteArray());
        ListenableFuture<SendResult<Long,byte[]>> future = kafkaTemplate.send(record);

        future.addCallback(new ListenableFutureCallback<SendResult<Long,byte[]>>() {
            public void onSuccess(SendResult<Long,byte[]> result) {
                //todo - handle success
            }
            public void onFailure(Throwable ex) {
                //todo - handle failure
            }
        });
    }

}
