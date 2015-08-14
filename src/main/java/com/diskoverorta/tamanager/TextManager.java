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

import java.io.ByteArrayInputStream;
import com.diskoverorta.entities.EntityManager;
//import com.diskoverorta.lifesciences.LSInterface;
import com.diskoverorta.osdep.StanfordNLP;
import com.diskoverorta.utils.EntityUtils;
import com.diskoverorta.vo.*;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.diskoverorta.pyinterface.*;
//import com.serendio.diskoverer.lifesciences.document.LifeScienceDocument;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Scanner;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import java.io.IOException;  
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.io.*;

public class TextManager
{
	@Option(name = "-analyze", aliases = {"-analyze"}, usage = "Accepted options for analyze : Entity, Sentiment, Topic, Keyword, All")
	String analysis;

    @Option(name = "-Text", aliases = {"-text"}, usage = "Extracts Text analytics components for given Text")
	String text;

    @Option(name = "-File", aliases = {"-file"}, usage = "Extracts Text analytics components for given File")
    String fileName;

    @Argument
    List<String> arguments = new ArrayList<String>();
	
    static StanfordNLP nlpStanford = null;
    static ThriftClient pyClient = null;

    DocumentObject doc = null;

    public TextManager()
    {
        if(nlpStanford==null)
        {
            nlpStanford = new StanfordNLP();
        }
        if(pyClient==null)
        {
            pyClient = new ThriftClient("localhost",8002);
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

    public String tagUniqueTextAnalyticsComponentsINJSON(String sDoc,TAConfig config) throws UnsupportedEncodingException
    {
        String jsonOutput = "";
        APIOutput apiOut = new APIOutput();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
       if(config.analysisConfig != null && config.analysisConfig.get("Entity") == "TRUE")
        {
           
            DocSentObject doc = tagTextAnalyticsComponents(sDoc, config);
            DocumentObject docObject = aggregateDocumentComponentsFromSentences(doc);
            EntityAPISet apiSet = EntityUtils.getEntitySet(docObject);
            apiOut.Entity = apiSet;
        }

       // if(config.analysisConfig.get("LSEntity") == "TRUE")
       //     apiOut.entity_lifesciences = gson.fromJson(LSInterface.getLSEntitiesinJSON(sDoc),LifeScienceDocument.class);
        if(config.analysisConfig.get("Topic") == "TRUE")
        {
          
            Set<String> topic_set = new TreeSet<String>();
            if (apiOut.Topics == null)
                //apiOut.Topics = new TextInformation();
            if(pyClient != null)
            {

                
              //  InputStream is = new ByteArrayInputStream(sDoc.getBytes("UTF-8"));
		byte[] ptext = sDoc.getBytes("UTF-8");
		//String inputStreamString = new Scanner(is,"UTF-8").next();
		//System.out.println("====>"+new String(ptext, "UTF-8"));
                List<String> topics = pyClient.getTopics(new String(ptext, "UTF-8"));
                System.out.println("====>"+topics);
                for (String topic : topics)
                    topic_set.add(topic);
            }
            if (topic_set.isEmpty() == true)
            {
                topic_set.add("Topic analyzer not working, Start Thrift server at port 8002");
            }
            apiOut.Topics = topic_set;
        }

       if(config.analysisConfig != null && config.analysisConfig.get("Keyword") == "TRUE")
        {
           
            Set<String> keyword_set = new TreeSet<String>();
            if (apiOut.Keywords == null)
                //apiOut.Keywords = new TextInformation();
            if(pyClient != null)
            {
                
                List<String> keywords = pyClient.getKeywords(sDoc);
                for (String keyword : keywords)
                    keyword_set.add(keyword);
            }
            if (keyword_set.isEmpty() == true)
            {
                keyword_set.add("Keyword extractor not working, Start Thrift server at port 8002");
            }
            apiOut.Keywords = keyword_set;
        }

     if(config.analysisConfig != null && config.analysisConfig.get("Sentiment") == "TRUE")
        {
            String senti_temp = null;
            Set<String> senti_set = new TreeSet<String>();

            if (apiOut.Sentiment == null)
               // apiOut.Sentiment = new TextInformation();

            if(pyClient != null)
            {
                if (apiOut.Sentiment == null)
                    //apiOut.Sentiment = new TextInformation();

                if (config.sentimentConfig.get("textType") == "blogs_news")
                    senti_temp = pyClient.getSentimentScore(sDoc, config.sentimentConfig.get("title"), config.sentimentConfig.get("middleParas"), config.sentimentConfig.get("lastPara"), 1);
                if (config.sentimentConfig.get("textType") == "reviews")
                    senti_temp = pyClient.getSentimentScore(sDoc, config.sentimentConfig.get("title"), config.sentimentConfig.get("topDomain"), config.sentimentConfig.get("subDomain"));
                else
                    senti_temp = pyClient.getSentimentScore(sDoc, config.sentimentConfig.get("textType"));
            }
            if (senti_temp == null)
            {
                senti_temp = "Sentiment analyzer not working, Start Thrift server at port 8002";
            }

            senti_set.add(senti_temp);
            apiOut.Sentiment = senti_set;
        }
        return gson.toJson(apiOut);
    }

    public static void main(String args[]) throws UnsupportedEncodingException 
    {
        TAConfig config = new TAConfig();
        TextManager temp = new TextManager();
        CmdLineParser parser = new CmdLineParser(temp);
        String sample = "Sachin Tendulkar won the Man of the Match award at Sydney in the year 2012:). Dhoni won the Man of the series award at Melbourne.";


        String trialtext = "";
        try {
            parser.parseArgument(args);
        }catch(CmdLineException ex)
        {
            ex.printStackTrace();
        }
        System.out.println("Command line API :");
        parser.printUsage(System.out);
        config.entityConfig.put("person", "TRUE");
        config.entityConfig.put("organization", "TRUE");
        config.entityConfig.put("location", "TRUE");
        config.entityConfig.put("date", "TRUE");
        config.entityConfig.put("time", "TRUE");
        config.entityConfig.put("currency", "TRUE");
        config.entityConfig.put("percent", "TRUE");

        if((temp.text == null) && (temp.fileName == null)) {
            trialtext = "Sachin Tendulkar won the Man of the Match award at Sydney in the year 2012:). Dhoni won the Man of the series award at Melbourne.";
            System.out.println("No Text source provided considering this default text for analysis : "+trialtext);
        }
        else if((temp.text != null) && (temp.fileName != null)) {
            System.out.println("Command line text input considered for Text Analytics : " + temp.text);
            trialtext = temp.text;
        }
        else if((temp.text != null)) {
            System.out.println("Command line text input considered for Text Analytics : " + temp.text);
            trialtext = temp.text;
        }
        else if((temp.fileName != null)) {
            System.out.println("Command line text input considered for Text Analytics");
            try {
                trialtext = Files.toString(new File(temp.fileName), Charsets.UTF_8);
            }catch(IOException ex)
            {
                ex.printStackTrace();
            }
            System.out.println("File input considered for Text Analytics : " + trialtext);
        }

        if((temp.analysis != null) && (temp.analysis.equalsIgnoreCase("Entity") == true)) {
            config.analysisConfig.put("Entity", "TRUE");
            System.out.println("Analyzing Entity in given text");
        }
        else if ((temp.analysis != null) && (temp.analysis.equalsIgnoreCase("Sentiment") == true)) {
            config.analysisConfig.put("Sentiment", "TRUE");
            System.out.println("Analyzing Sentiment in given text");
        }
        else if ((temp.analysis != null) && (temp.analysis.equalsIgnoreCase("Topic") == true)) {
            config.analysisConfig.put("Topic", "TRUE");
            System.out.println("Analyzing Topic in given text");
        }
        else if ((temp.analysis != null) && (temp.analysis.equalsIgnoreCase("Keyword") == true)) {
            config.analysisConfig.put("Keyword", "TRUE");
            System.out.println("Extracting keywords in given text");
        }
        else if ((temp.analysis != null) && (temp.analysis.equalsIgnoreCase("All") == true)) {
            System.out.println("Analyzing Entity, Sentiment, Topic and Keywords");
            config.analysisConfig.put("Entity","TRUE");
            config.analysisConfig.put("Sentiment", "TRUE");
            config.analysisConfig.put("Topic", "TRUE");
            config.analysisConfig.put("Keyword","TRUE");
        }
        else {
            System.out.println("Analysis options not specified. Possible values : Entity, Sentiment, Topic, Kaeyword, All");
            System.out.println("Choosing All by default");
            config.analysisConfig.put("Entity","TRUE");
            config.analysisConfig.put("Sentiment", "TRUE");
            config.analysisConfig.put("Topic", "TRUE");
            config.analysisConfig.put("Keyword","TRUE");
        }
        System.out.println("Text Analytics output : ");
    
        System.out.println(temp.tagUniqueTextAnalyticsComponentsINJSON(trialtext, config));

    }
 }

  
      
        
       
      
        
        
        
      

    

       

    




