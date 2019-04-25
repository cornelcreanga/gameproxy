package com.ccreanga.gameproxy;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ServerConfig {

    @Value("${server.incoming.port}")
    private int incomingPort;

    @Value("${server.outgoing.port}")
    private int outgoingPort;

}
