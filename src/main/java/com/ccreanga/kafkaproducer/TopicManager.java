package com.ccreanga.kafkaproducer;

import com.ccreanga.kafkaproducer.gateway.TopicStorage;
import com.ccreanga.kafkaproducer.incoming.IncomingMessage;
import java.io.ByteArrayOutputStream;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

@Component
public class TopicManager {

    private Producer<Long, byte[]> producer;

    @Autowired
    private TopicStorage topicStorage;

    @Autowired
    private KafkaConfig kafkaConfig;

    public void sendMessage(IncomingMessage message){

        try {
            List<String> topics = topicStorage.getTopics(message.getMatchId());
            for (String next : topics) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
                message.writeExternal(baos);
                ProducerRecord<Long, byte[]> record = new ProducerRecord<>(next, message.getMatchId(), baos.toByteArray());
                producer.send(record, (metadata, exception) -> {
                    if (metadata != null) {
                        //success
                    } else {
                        exception.printStackTrace();
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @PostConstruct
    public void init() {
        producer = createProducer();
    }

    private Producer<Long, byte[]> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getKafkaBootstrapServers());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16 * 1024);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        return new KafkaProducer<>(props);
    }
}
