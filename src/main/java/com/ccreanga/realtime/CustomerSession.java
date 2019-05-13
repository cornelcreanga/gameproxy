package com.ccreanga.realtime;

import com.ccreanga.protocol.outgoing.server.ServerMsg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSession {

    private Customer customer;
    private BlockingQueue<ServerMsg> messageQueues;
    private Socket socket;

}
