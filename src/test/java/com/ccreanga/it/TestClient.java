package com.ccreanga.it;

import com.ccreanga.gameproxy.outgoing.message.server.DataMsg;
import com.ccreanga.gameproxy.outgoing.message.server.InfoMsg;
import com.ccreanga.it.Consumer.DataMsgHandler;
import com.ccreanga.it.Consumer.InfoMsgHandler;
import java.util.ArrayList;
import java.util.UUID;

public class TestClient {

    public static void main(String[] args) throws Exception{
        int c = 1;
        Consumer[] consumers = new Consumer[c];
        for (int i = 0; i < c; i++) {
            Consumer consumer = new Consumer("test" + (i + 1), "127.0.0.1", 8082, new SimpleDataMsgHandler("test" + (i + 1)) , new SimpleInfoMsgHandler());
            consumers[i] = consumer;
            new Thread(consumers[i]).start();
        }
        long t1=System.currentTimeMillis();
        Producer producer = new Producer("127.0.0.1", 8081);
        for (int i = 0; i <1000000 ; i++) {
            UUID uuid1 = UUID.randomUUID();
            producer.produce(uuid1, 1L, "some message "+i);
        }
        producer.close();
        long t2=System.currentTimeMillis();
        System.out.println("producing in "+(t2-t1));

//        for (int i = 0; i < 5; i++) {
//            consumers[i].stop();
//        }

    }

    static class SimpleDataMsgHandler implements DataMsgHandler{

        private String name;

        public SimpleDataMsgHandler(String name) {
            this.name = name;
        }

        int c = 0,i=0;
        long t1=System.currentTimeMillis(),t2;
        ArrayList<Integer> list = new ArrayList<>();
        @Override
        public void handle(DataMsg message) {
            //System.out.println("InfoMsg "+message.getCode());
            if ((System.currentTimeMillis()-t1)>1000){
                list.add(c);
                t1 = System.currentTimeMillis();
                c = 0;
            }
            c++;
            i++;
            if (i%250000==0){
                int sum = 0;
                for (Integer next : list) {
                    sum += next;
                }
                System.out.println(name+" - "+sum/list.size());
                list = new ArrayList<>();
                t1=System.currentTimeMillis();
            }
        }
    }
    static class SimpleInfoMsgHandler implements InfoMsgHandler{

        @Override
        public void handle(InfoMsg message) {
            System.out.println("InfoMsg "+message.getCode());
        }
    }

}
