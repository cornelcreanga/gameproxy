package com.ccreanga.kafkaproducer;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "server")
public class ServerConfig {

    @Value("${server.incomingMessage.port}")
    private int incomingMessagePort;

    @Value("${server.outgoingMessage.port}")
    private int outgoingMessagePort;

}
