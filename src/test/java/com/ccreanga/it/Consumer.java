package com.ccreanga.it;

import com.ccreanga.gameproxy.outgoing.message.MessageIO;
import com.ccreanga.gameproxy.outgoing.message.client.LoginMsg;
import com.ccreanga.gameproxy.outgoing.message.client.LogoutMsg;
import com.ccreanga.gameproxy.outgoing.message.server.DataEndMsg;
import com.ccreanga.gameproxy.outgoing.message.server.DataMsg;
import com.ccreanga.gameproxy.outgoing.message.server.InfoMsg;
import com.ccreanga.gameproxy.outgoing.message.server.ServerMsg;
import com.ccreanga.gameproxy.util.IOUtil;
import java.io.IOException;
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

            socket.setSoTimeout(50000);
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
            if (infoMsg.getCode()==120)
                throw new RuntimeException("login failed ");
            if (infoMsg.getCode()==110){
                System.out.println("already authenticated");
            }
            if ((infoMsg.getCode()!=110) && (infoMsg.getCode()!=100)){
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
            e.printStackTrace();
        }
    }

    public synchronized void stop() {
        isStopped = true;
        try {
            MessageIO.serializeClientMsg(new LogoutMsg(name),socket.getOutputStream());
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
