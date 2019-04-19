package com.ccreanga.gameproxy.outgoing.message.server;

import com.ccreanga.gameproxy.incoming.IncomingMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataMessage extends AbstractMessage {

    private IncomingMessage message;


}
