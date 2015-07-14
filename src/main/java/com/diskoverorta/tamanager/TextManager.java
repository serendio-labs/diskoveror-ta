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
package com.diskoverorta.tamanager;

import com.diskoverorta.entities.EntityManager;
//import com.diskoverorta.lifesciences.LSInterface;
import com.diskoverorta.osdep.StanfordNLP;
import com.diskoverorta.utils.EntityUtils;
import com.diskoverorta.vo.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.serendio.diskoverer.lifesciences.document.LifeScienceDocument;
import com.diskoverorta.topicmodel.Client;
import com.diskoverorta.sentiment.*;


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.kohsuke.args4j.ExampleMode.ALL;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;



//import java.io.File;  
import java.io.IOException;  
import java.util.ArrayList;  



/**
 * Created by praveen on 11/11/14.
 */
public class TextManager
{
	 
	@Option(name = "--use-Entity", aliases = {"-e"}, usage = "if entity is required")
	  private boolean Entity;
	
	@Option(name = "--use-Sentiment", aliases = {"-s"}, usage = "if sentiment is required")
	  private boolean Sentiment;
	
	@Option(name = "--use-Topic", aliases = {"-t"}, usage = "if topic is required")
	  private boolean Topic;
	
	@Option(name = "--use-All", aliases = {"-all"}, usage = "if entity, sentiment, topic  is required")
	  private boolean All;
	
	@Option(name = "--use-Text", aliases = {"-text"}, usage = "if external text is required")
	  private boolean Text;
	
	@Argument  
          private List<String> arguments = new ArrayList<String>(); 
	
    static StanfordNLP nlpStanford = null;
    static Client TopicThriftClient = null;
    static SClient SentimentThriftClient = null;
    DocumentObject doc = null;
    
    public static void main(String args[])throws IOException
    {
    	 new TextManager().doMain(args);  
    }
    
    public TextManager()
    {
        if(nlpStanford==null)
        {
            nlpStanford = new StanfordNLP();
        }
        if(TopicThriftClient==null)
        {
            TopicThriftClient = new Client("localhost",8001);
        }
        if(SentimentThriftClient==null)
        {
             SentimentThriftClient = new SClient("localhost",8002);
        }
    }

    public DocSentObject tagTextAnalyticsComponents(String sDoc,TAConfig config)
    {
        DocSentObject doc = new DocSentObject();
        List<String> sentList = nlpStanford.splitSentencesINDocument(sDoc);

        for(int i=0; i < sentList.size(); i++)
        {
            SentenceObject temp = new SentenceObject();
            temp.sentenceText = sentList.get(i);

            if(config.analysisConfig.get("Entity") == "TRUE")
                temp.entities = (new EntityManager().getSelectedEntitiesForSentence(temp.sentenceText,config.entityConfig));

            doc.docSentences.add(temp);
        }
        return doc;
    }

    public DocumentObject aggregateDocumentComponentsFromSentences(DocSentObject docSent)
    {
        DocumentObject docObject = new DocumentObject();
        for (SentenceObject sentObj : docSent.docSentences)
        {
            docObject.entities.currency.addAll(sentObj.entities.currency);
            docObject.entities.date.addAll(sentObj.entities.date);
            docObject.entities.location.addAll(sentObj.entities.location);
            docObject.entities.organization.addAll(sentObj.entities.organization);
            docObject.entities.percent.addAll(sentObj.entities.percent);
            docObject.entities.person.addAll(sentObj.entities.person);
            docObject.entities.time.addAll(sentObj.entities.time);
        }
        docObject.entitiesMeta = EntityUtils.extractEntityMap(docObject.entities);
        return docObject;
    }

    public String tagTextAnalyticsComponentsINJSON(String sDoc,TAConfig config)
    {
        DocSentObject doc = tagTextAnalyticsComponents(sDoc,config);
        DocumentObject docObject = aggregateDocumentComponentsFromSentences(doc);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(docObject);
        return jsonOutput;
    }

    public String tagUniqueTextAnalyticsComponentsINJSON(String sDoc,TAConfig config)
    {
        String jsonOutput = "";
        APIOutput apiOut = new APIOutput();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if(config.analysisConfig.get("Entity") == "TRUE")
        {
            DocSentObject doc = tagTextAnalyticsComponents(sDoc, config);
            DocumentObject docObject = aggregateDocumentComponentsFromSentences(doc);
            EntityAPISet apiSet = EntityUtils.getEntitySet(docObject);
            apiOut.entity_general = apiSet;
        }

       // if(config.analysisConfig.get("LSEntity") == "TRUE")
       //     apiOut.entity_lifesciences = gson.fromJson(LSInterface.getLSEntitiesinJSON(sDoc),LifeScienceDocument.class);
        if(config.analysisConfig.get("Topic") == "TRUE")
        {
            Set<String> topic_set = new TreeSet<String>();
            if (apiOut.text_information == null)
                apiOut.text_information = new TextInformation();
            if(TopicThriftClient != null)
            {
                String topics_temp = TopicThriftClient.getTopics(sDoc);
                String[] topics = topics_temp.split("\\|");
                for (String topic : topics)
                    topic_set.add(topic);
            }
            else
            {
                topic_set.add("Topic analyzer not working, Start Topic Thrift server at port 8001");
            }
            apiOut.text_information.topics = topic_set;
        }
        if(config.analysisConfig.get("Sentiment") == "TRUE")
        {
            String senti_temp = null;
            Set<String> senti_set = new TreeSet<String>();

            if (apiOut.text_information == null)
                apiOut.text_information = new TextInformation();

            if(SentimentThriftClient != null)
            {
                if (apiOut.text_information == null)
                    apiOut.text_information = new TextInformation();
                if (config.sentimentConfig.get("textType") == "blogs_news")
                    senti_temp = SentimentThriftClient.getSentimentScore(sDoc, config.sentimentConfig.get("title"), config.sentimentConfig.get("middleParas"), config.sentimentConfig.get("lastPara"), 1);
                if (config.sentimentConfig.get("textType") == "reviews")
                    senti_temp = SentimentThriftClient.getSentimentScore(sDoc, config.sentimentConfig.get("title"), config.sentimentConfig.get("topDomain"), config.sentimentConfig.get("subDomain"));
                else
                    senti_temp = SentimentThriftClient.getSentimentScore(sDoc, config.sentimentConfig.get("textType"));
            }
            else
            {
                senti_temp = "Sentiment analyzer not working, Start Sentiment Thrift server at port 8002";
            }

            senti_set.add(senti_temp);
            apiOut.text_information.sentiment = senti_set;
        }
        return gson.toJson(apiOut);
    }
    
    public void doMain(String[] args) throws IOException { 
    	
        CmdLineParser parser = new CmdLineParser(this);
        TAConfig config = new TAConfig();
        TextManager temp = new TextManager();
        String sample = "Sachin Tendulkar was the winner of the WorldCup sporting event at South Africa during the year 2014 :)";
        String sample1 = "";
        for(int i=0;i<args.length;i++)
        {
        	sample1 = sample1 + args[i] + ",";
        	
        }
        sample1 = sample1.substring(0,sample1.length()-1);
        System.out.println("args length:" + args.length);
    	
        
        config.analysisConfig.put("Entity", "FALSE");
        
        config.analysisConfig.put("Topic", "FALSE");
        
        config.analysisConfig.put("Sentiment", "FALSE");
        
        config.entityConfig.put("Person", "TRUE");
        
        config.entityConfig.put("Organization", "TRUE");
        
        config.entityConfig.put("Location", "TRUE");
        
        config.entityConfig.put("Date", "TRUE");
        
        config.entityConfig.put("Time", "TRUE");
        
        config.entityConfig.put("Currency", "TRUE");
        
        config.entityConfig.put("Percent", "TRUE");
       
       
          
          try {  
             
            parser.parseArgument(args);  
  
          if( arguments.isEmpty() )  
                throw new CmdLineException("No argument is given");  
  
        } catch( CmdLineException e ) {  
            
            System.err.println(e.getMessage());  
            System.err.println("java TextManager [options...] arguments...");  
            parser.printUsage(System.err);  
            System.err.println();  
            System.err.println(" Example: java -jar diskoverorta-0.1.jar"+parser.printExample(ALL));  
  
        }  
        
    
        if( Entity)
        {
            config.analysisConfig.put("Entity", "TRUE");
            System.out.println("-e flag is set" + temp.tagUniqueTextAnalyticsComponentsINJSON(sample, config)) ;  
        }
        if( Topic)
        {
            
            config.analysisConfig.put("Topic", "TRUE");
            System.out.println("-c flag is set" + temp.tagUniqueTextAnalyticsComponentsINJSON(sample, config)); 
        }
        if( Sentiment)
        {
            config.analysisConfig.put("Sentiment", "TRUE");
            System.out.println("-s flag is set" + temp.tagUniqueTextAnalyticsComponentsINJSON(sample, config)); 
        }  
        if( All)
        {
            config.analysisConfig.put("Entity", "TRUE");
            config.analysisConfig.put("Topic", "TRUE");
            config.analysisConfig.put("Sentiment", "TRUE");
            System.out.println("-all flag is set" + temp.tagUniqueTextAnalyticsComponentsINJSON(sample, config)); 
        }
        if( Text)
        {
            config.analysisConfig.put("Entity", "TRUE");
            config.analysisConfig.put("Topic", "TRUE");
            config.analysisConfig.put("Sentiment", "TRUE");
            System.out.println("-text flag is set" + temp.tagUniqueTextAnalyticsComponentsINJSON(sample1, config)); 
        }
        
        }
        
        }

        
  
  
      
        
       
      
        
        
        
      

    

       

    




