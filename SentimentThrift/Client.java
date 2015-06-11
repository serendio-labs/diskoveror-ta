import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import javaThrift.*;

import java.io.*;
import java.util.*;

public class Client {

    public void startClient() {
        TTransport transport;
        try {
            transport = new TSocket("localhost", 8002);
            TProtocol protocol = new TBinaryProtocol(transport);
            
	    Sentiments.Client client = new Sentiments.Client(protocol);
            transport.open();

            System.out.println("Client is UP ! ;)");

            System.out.println("Enter Text to find Sentiment : ");

            Scanner s = new Scanner(System.in);

            String text = s.nextLine();

            String senti = client.getSentiment(text);

            System.out.println(senti);

            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startClient();
    }
}

