package com.diskoverorta.pyinterface;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by praveen on 5/1/15.
 */
public class SIntelClient {
    static SIntelInterface.Client client = null;
    static TTransport transport = null;
    SIntelClient()
    {
        try
        {
            transport = new TSocket("localhost",30303);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            client = new SIntelInterface.Client(protocol);
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    protected void finalize()
    {
        if(transport!=null)
        {
            transport.close();
        }
    }
    String extractIAB(String inputText)
    {
        String result = null;
        try
        {
            result = client.extractIAB(inputText);
        }
        catch(TException ex)
        {
            ex.printStackTrace();
        }
        return result;
    }
    String extractGENDER(String inputText)
    {
        String result = null;
        try
        {
            result = client.extractGENDER(inputText);
        }
        catch(TException ex)
        {
            ex.printStackTrace();
        }
        return result;
    }
    String extractActivity(String inputText)
    {
        String result = null;
        try
        {
            result = client.findActivity(inputText);
        }
        catch(TException ex)
        {
            ex.printStackTrace();
        }
        return result;
    }
    public static void main(String args[])
    {
        SIntelClient obj = new SIntelClient();
        System.out.println(obj.extractGENDER("praveen"));
        System.out.println(obj.extractIAB("I like iphone"));
        System.out.println(obj.extractActivity("I sold my iphone to buy samsung"));
    }
}
