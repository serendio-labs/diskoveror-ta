
package com.diskoverorta.topicmodel;

/**
 *
 * @author satish palaniappan
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
    
    public String getTopics(String text) {
        TTransport transport;
        try {
            transport = new TSocket(this.ip, this.port);
            TProtocol protocol = new TBinaryProtocol(transport);
            
	    Categorizer.Client client = new Categorizer.Client(protocol);
            transport.open();

            String cat = client.getTopic(text);

            transport.close();
            
            return cat;
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
        return "Connection to " + this.ip + ":" + this.port + " failed!";
 }
}