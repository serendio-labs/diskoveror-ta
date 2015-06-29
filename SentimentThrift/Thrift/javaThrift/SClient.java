/**
 * Copyright 2015 Satish Palaniappan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class SClient {
    String ip;
    int port;
    TTransport transport = null;
    Sentiments.Client client = null;
    TProtocol protocol = null;

    public SClient(String s, int p){
        this.ip= s;
        this.port = p;
        if (transport == null)
            transport = new TSocket(this.ip, this.port);
        if (protocol == null)
            {
                protocol = new TBinaryProtocol(transport);
                client = new Sentiments.Client(protocol);
                try {
                    transport.open();
                } catch (TTransportException e) {
                    e.printStackTrace();
                }
            }

    }

    public String getSentimentScore(String mainText,String textType) {
        try {

            SentiRequestObject obj = new SentiRequestObject();
            obj.setMainText(mainText);
            obj.setTextType(textType);

            int senti = client.getSentimentScore(obj);

            return ("" + senti);
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

    public String getSentimentScore(String mainText,String title,String topDomain,String subDomain) {
        try {
            SentiRequestObject obj = new SentiRequestObject();
            obj.setMainText(mainText);
            obj.setTextType("reviews");
            obj.setTitle(title);
            obj.setTopDomain(topDomain);
            obj.setSubDomain(subDomain);

            int senti = client.getSentimentScore(obj);

            return ("" + senti);

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
    public String getSentimentScore(String mainText,String title,String middleParas,String lastPara, int diffBlog) {

        // "diffBlog" parameter can be set with any integer (added as a dummy parameter to support method overloading)

        try {

            SentiRequestObject obj = new SentiRequestObject();
            obj.setMainText(mainText);
            obj.setTextType("blogs_news");
            obj.setTitle(title);
            obj.setMiddleParas(middleParas);
            obj.setLastPara(lastPara);

            int senti = client.getSentimentScore(obj);

            return ("" + senti);

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
//
//   public static void main(String [] Args)
//   {
//     SClient c = new SClient("localhost",8002);
//     System.out.println(c.getSentimentScore("This phone is shit !","microblogs"));
//
//   }

}
