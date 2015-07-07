
package com.diskoverorta.topicmodel;

/*******************************************************************************
 *   Copyright 2015 Serendio Inc. ( http://www.serendio.com/ )
 *   Author - Praveen Jesudhas
 *    
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/

import com.diskoverorta.sentiment.Sentiments;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;


public class Client {

    String ip;
    int port;
    TTransport transport = null;
    Categorizer.Client client = null;
    TProtocol protocol = null;
    public Client(String s, int p){
        this.ip= s;
        this.port = p;
        if (transport == null) {
            transport = new TSocket(this.ip, this.port);
            try {
                transport.open();
            } catch (TTransportException e) {
                e.printStackTrace();
                transport.close();
            }
        }
        if (protocol == null)
        {
            protocol = new TBinaryProtocol(transport);
            client = new Categorizer.Client(protocol);
        }
    }
    
    public String getTopics(String text) {
        try {

            String cat = client.getTopic(text);
            return cat;
        } catch (TTransportException e) {
            e.printStackTrace();
            transport.close();
        } catch (TException e) {
            e.printStackTrace();
            transport.close();
        }
        transport.close();
        return "Connection to " + this.ip + ":" + this.port + " failed!";
 }
}