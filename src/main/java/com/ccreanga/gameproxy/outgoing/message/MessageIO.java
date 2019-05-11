package com.ccreanga.gameproxy.outgoing.message;

import com.ccreanga.gameproxy.outgoing.message.client.*;
import com.ccreanga.gameproxy.outgoing.message.server.DataEndMsg;
import com.ccreanga.gameproxy.outgoing.message.server.DataMsg;
import com.ccreanga.gameproxy.outgoing.message.server.InfoMsg;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

public class MessageIO {

    public static final short LOGIN = 1;
    public static final short LOGOUT = 2;
    public static final short HISTORICAL_DATA = 3;

    public static void serializeClientMsg(ClientMsg clientMsg, OutputStream out) throws IOException{
        out.write(clientMsg.getType());
        clientMsg.writeExternal(out);
    }

    public static Optional<ClientMsg> deSerializeClientMsg(InputStream in) throws IOException {
        int type = in.read();
        switch (type){
            case ClientMsg.LOGIN: return Optional.of(LoginMsg.readExternal(in));
            case ClientMsg.LOGOUT:return Optional.of(LogoutMsg.readExternal(in));
            case ClientMsg.HISTORICAL_DATA:return Optional.of(HistoryDataMsg.readExternal(in));
            case -1:return Optional.empty();
            default:throw new MalformedException("can't parse message type "+type,"bad_message_type");
        }
    }

    public static void serializeServerMsg(ServerMsg serverMsg, OutputStream out) throws IOException{
        out.write(serverMsg.getType());
        serverMsg.writeExternal(out);
    }

    public static Optional<ServerMsg> deSerializeServerMsg(InputStream in) throws IOException{
        int type = in.read();
        switch (type){
            case ServerMsg.DATA: return Optional.of(DataMsg.readExternal(in));
            case ServerMsg.INFO:return Optional.of(InfoMsg.readExternal(in));
            case ServerMsg.DATA_END:return Optional.of(DataEndMsg.readExternal(in));

            case -1:return Optional.empty();
            default:throw new MalformedException("can't parse message type "+type,"bad_message_type");
        }
    }


}
