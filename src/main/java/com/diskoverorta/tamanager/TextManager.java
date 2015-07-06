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

/**
 * Created by praveen on 11/11/14.
 */
public class TextManager
{
    static StanfordNLP nlpStanford = null;
    static Client TopicThriftClient = null;
    static SClient SentimentThriftClient = null;
    DocumentObject doc = null;
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
        if(config.analysisConfig.get("Category") == "TRUE")
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

    public static void main(String args[])
    {
        String sample = "Investigation into the Saradha chit fund scam has so far not revealed any terror link with Bangladesh, the Centre said today, days after the BJP had alleged such a connection.\n" +
            "\n" +
            "“The investigation has so far not revealed any such transaction where money was routed to Bangladesh to fund terrorist activities,” Union Minister of State for Personnel Jitendra Singh told the Lok Sabha in a written response.\n" +
            "\n" +
            "BJP chief Amit Shah had alleged that Saradha chit fund money had been used in the October 2, 2014 Bardhaman blast, which is being probed for link with the Jamaat-ul-Mujahideen Bangladesh (JMB) terror outfit.\n" +
            "\n" +
            "“Saradha chit fund money was used in the Burdwan (Bardhaman) blast. The NIA is not being allowed to probe the blast properly. Hurdles are being created. It is being done in order to save TMC leaders who are involved in the blast,” Mr. Shah had said, attacking the Trinamool Congress, at a BJP rally in Kolkata.\n" +
            "\n" +
            "The Union Minister was asked whether the government has sought details of the probe into the Saradha chit fund scam after reports indicated that a part of the money was routed to Bangladesh to fund terror activities. Mr. Singh replied that government had not sought details of the probe.\n" +
            "\n" +
            "To another question on whether the Saradha chief has admitted that he paid large sums to several people to influence the case in his favour, the Minister said, “The matter is under investigation.”";
        TAConfig config = new TAConfig();
        TextManager temp = new TextManager();


        config.analysisConfig.put("Entity", "TRUE");
        config.analysisConfig.put("Category", "TRUE");
        config.analysisConfig.put("Sentiment", "TRUE");

        config.entityConfig.put("Person", "TRUE");
        config.entityConfig.put("Organization", "TRUE");
        config.entityConfig.put("Location", "TRUE");
        config.entityConfig.put("Date", "TRUE");
        config.entityConfig.put("Time", "TRUE");
        config.entityConfig.put("Currency", "TRUE");
        config.entityConfig.put("Percent", "TRUE");

        if (args.length == 0)
        {
            System.out.println("Sample input text not found, finding text analytics components with below Text :");
            System.out.println(sample);
            System.out.println("Execution syntax : java -jar diskoverorta-0.1.jar SampleText");
        }
        if(args.length == 1)
            sample = args[1];
        System.out.println(temp.tagUniqueTextAnalyticsComponentsINJSON(sample, config));
    }

}
