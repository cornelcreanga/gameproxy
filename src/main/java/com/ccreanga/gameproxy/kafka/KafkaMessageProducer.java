package com.ccreanga.gameproxy.kafka;

import com.ccreanga.gameproxy.incoming.IncomingMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
@EnableAsync
public class KafkaMessageProducer {

    @Autowired
    private KafkaTemplate<Long, byte[]> kafkaTemplate;

    @Async
    public void sendAsynchToKafka(String topic,IncomingMessage message) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
        message.writeExternal(baos);
        ProducerRecord<Long, byte[]> record = new ProducerRecord<>(topic, message.getMatchId(), baos.toByteArray());
        ListenableFuture<SendResult<Long,byte[]>> future = kafkaTemplate.send(record);

        future.addCallback(new ListenableFutureCallback<SendResult<Long,byte[]>>() {
            public void onSuccess(SendResult<Long,byte[]> result) {
                log.trace("Message {} sent succesfully to kafka", message.getId());
                //todo - handle success
            }

            public void onFailure(Throwable ex) {
                log.trace("Message {} sent failure, exception {}", message.getId(), ex.getMessage());
                //todo - handle failure
            }
        });
    }

}
