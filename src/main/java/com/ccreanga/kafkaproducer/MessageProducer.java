package com.ccreanga.kafkaproducer;

import com.ccreanga.kafkaproducer.incoming.IncomingMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class MessageProducer implements CommandLineRunner {

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private KafkaTemplate kafkaTemplate;//not used for the moment

    @Value("${kafka.topic.topicName}")
    private String topicName;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    public static void main(String[] args) {
        SpringApplication.run(MessageProducer.class, args);
    }

    @Override
    public void run(String... args) {
        int messages = 1_00_000;
        long t1 = System.currentTimeMillis();
        try {
            runProducer(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long t2 = System.currentTimeMillis();
        System.out.printf("Produced %d messages in %d seconds",messages,(t2-t1)/1000);
    }

    private Producer<Long, byte[]> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16 * 1024);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    private void runProducer(int sendMessageCount) throws IOException {
        long time = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(sendMessageCount);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
        try (Producer<Long, byte[]> producer = createProducer()) {
            for (long i = 0; i < sendMessageCount; i++) {
                IncomingMessage message = new IncomingMessage(i, (int)(Math.random()*10), ("test message " + i).getBytes(), System.currentTimeMillis());
                message.writeExternal(baos);
                final ProducerRecord<Long, byte[]> record = new ProducerRecord<>(topicName, message.getMatchId(), baos.toByteArray());
                baos.reset();
                producer.send(record, (metadata, exception) -> {
                    long elapsedTime = System.currentTimeMillis() - time;
                    if (metadata != null) {
                        //success
                    } else {
                        exception.printStackTrace();
                    }
                    countDownLatch.countDown();
                });
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException ignored) {
            }
        }
    }

}
