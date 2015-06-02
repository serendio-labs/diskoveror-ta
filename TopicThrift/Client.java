import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

//import javaThrift.Categorizer.*;
import javaThrift.*;

import java.io.*;
import java.util.*;

public class Client {

    public void startClient() {
        TTransport transport;
        try {
            transport = new TSocket("localhost", 8001);
            TProtocol protocol = new TBinaryProtocol(transport);
            
	    Categorizer.Client client = new Categorizer.Client(protocol);
            transport.open();

            System.out.println("Client is UP ! ;)");

            System.out.println("Enter Text to Categorize : ");

            Scanner s = new Scanner(System.in);

            String text = s.nextLine();

            String cat = client.getTopic(text);

            System.out.println(cat);

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

