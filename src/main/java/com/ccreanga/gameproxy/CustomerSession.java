package com.ccreanga.gameproxy;

import com.ccreanga.gameproxy.incoming.IncomingMessage;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSession {

    private Customer customer;
    private ArrayBlockingQueue<IncomingMessage> messageQueues;
    private Socket socket;

}
