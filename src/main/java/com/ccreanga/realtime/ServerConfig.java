package com.ccreanga.realtime;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ServerConfig {

    @Value("${serverConfig.incoming.port}")
    private int incomingPort;

    @Value("${serverConfig.realtime.port}")
    private int realtimePort;

}
