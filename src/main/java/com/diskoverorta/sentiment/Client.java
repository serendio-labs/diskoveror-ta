package com.diskoverorta.sentiment;

/**
 * Created with IntelliJ IDEA.
 * User: satish
 * Date: 11/6/15
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class Client {
    String ip;
    int port;

    public Client(String s, int p){
        this.ip= s;
        this.port = p;
    }

    public String getSentiment(String text) {
        TTransport transport;
        try {

            transport = new TSocket(this.ip, this.port);
            TProtocol protocol = new TBinaryProtocol(transport);

            Sentiments.Client client = new Sentiments.Client(protocol);
            transport.open();

            double senti = client.getSentiment(text);

            transport.close();

            return ("" + senti);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
        return "Connection to " + this.ip + ":" + this.port + " failed!";
    }


    public static void main(String [] Args)
    {
      Client c = new Client("localhost",8002);
      System.out.println(c.getSentiment("i am in love"));

    }

}
