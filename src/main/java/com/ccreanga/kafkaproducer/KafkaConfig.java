package com.ccreanga.kafkaproducer;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfig {

    @Value("${kafka.topic.topicName}")
    private String topicName;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;
}
