package com.ccreanga.it;

import com.ccreanga.protocol.outgoing.MessageIO;
import com.ccreanga.protocol.outgoing.client.LoginMsg;
import com.ccreanga.protocol.outgoing.client.LogoutMsg;
import com.ccreanga.protocol.outgoing.server.DataEndMsg;
import com.ccreanga.protocol.outgoing.server.DataMsg;
import com.ccreanga.protocol.outgoing.server.InfoMsg;
import com.ccreanga.protocol.outgoing.server.ServerMsg;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

public class Consumer implements Runnable {

    private Socket socket;
    private String name;
    private String ip;
    private int port;
    private boolean isStopped = false;
    private DataMsgHandler dataMsgHandler;
    private InfoMsgHandler infoMsgHandler;

    public Consumer(String name, String ip, int port, DataMsgHandler dataMsgHandler, InfoMsgHandler infoMsgHandler) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.dataMsgHandler = dataMsgHandler;
        this.infoMsgHandler = infoMsgHandler;
    }

    @Override
    public void run() {
        try{
            socket = new Socket(ip, port);

            socket.setSoTimeout(500000);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            MessageIO.serializeClientMsg(new LoginMsg(name),out);
            long t1 = System.currentTimeMillis();
            ServerMsg serverMsg = MessageIO.deSerializeServerMsg(in).get();
            long t2 = System.currentTimeMillis();
            System.out.println("login in "+(t2-t1));
            if (!(serverMsg instanceof InfoMsg)){
                throw new RuntimeException("expecting InfoMsg, found "+serverMsg.getClass());
            }
            InfoMsg infoMsg = (InfoMsg)serverMsg;
            if (infoMsg.getCode()==InfoMsg.UNAUTHORIZED)
                throw new RuntimeException("login failed ");
            if (infoMsg.getCode()==InfoMsg.ALREADY_AUTHENTICATED){
                System.out.println("already authenticated");
            }
            if ((infoMsg.getCode()!=InfoMsg.ALREADY_AUTHENTICATED) && (infoMsg.getCode()!=InfoMsg.AUTHORIZED)){
                throw new RuntimeException("unknown type "+infoMsg.getCode());
            }


            while(!isStopped){
                Optional<ServerMsg> optional = MessageIO.deSerializeServerMsg(in);
                if (optional.isEmpty())
                    break;
                ServerMsg message = optional.get();
                if (message instanceof DataEndMsg) {
                    System.out.println("DataEndMsg");
                    break;
                }
                if (message instanceof DataMsg)
                    dataMsgHandler.handle((DataMsg) message);
                if (message instanceof InfoMsg)
                    infoMsgHandler.handle((InfoMsg) message);

            }


        }catch (Exception e){
            System.out.println(name);
            e.printStackTrace();
        }
    }

    public synchronized void stop() {
        isStopped = true;
        try {
            MessageIO.serializeClientMsg(new LogoutMsg(),socket.getOutputStream());
            //todo investigate
            socket.close();
            //IOUtil.closeSocketPreventingReset(socket);
        } catch (Exception e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    public interface DataMsgHandler{
        void handle(DataMsg message);
    }
    public interface InfoMsgHandler{
        void handle(InfoMsg message);
    }

}
