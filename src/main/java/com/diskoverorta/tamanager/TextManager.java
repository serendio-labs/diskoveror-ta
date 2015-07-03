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
        String sample = " The CBI on Thursday filed the case diary and crime files in a sealed cover in a coal blocks allocation case related to Hindalco before a special court, complying with its order.\n" +
                "\n" +
                "The court has now fixed the matter for consideration of the CBI’s closure report filed in the case for December 12.\n" +
                "\n" +
                "Two days after the court had directed it to furnish the case diary of the matter, the CBI submitted two bundles of documents in a sealed cover before the court.\n" +
                "\n" +
                "“In compliance of the court’s order, we are filing the crime folder as well as the case diary,” senior Public Prosecutor V.K. Sharma told special CBI Judge Bharat Parashar.\n" +
                "\n" +
                "The judge said, “IO (Investigating officer) states that he has brought both the case diary and crime files in a sealed cover. He is further being told to assist the court in looking into the relevant papers. The matter is now adjourned for consideration on December 12.”\n" +
                "\n" +
                "During the brief hearing, the court said if there is still any clarification to be sought in the matter, it will ask the agency and then pass order on the closure report.\n" +
                "\n" +
                "The court was hearing a case in which an FIR was lodged against industrialist Kumar Mangalam Birla, ex-Coal Secretary P.C. Parakh and others relating to allocation of Talabira II and III coal blocks in Odisha in 2005 to Hindalco. The CBI had later on filed closure report in the case.\n" +
                "\n" +
                "During the hearing on November 25, the CBI had come in for some tough questioning from the court which had asked why the agency did not question former Prime Minister Manmohan Singh who was also holding the Coal portfolio between 2005 and 2009.\n" +
                "\n" +
                "The court’s observations had came after the CBI submitted that though initially it felt Singh’s examination was required, later it was found to be not necessary.\n" +
                "\n" +
                "At the end of the hearing, the court had summoned the case diary and crime files in a sealed cover and had posted the matter for on Thursday.\n" +
                "\n" +
                "Dr. Singh was holding the coal portfolio when Mr. Birla’s firm Hindalco was allocated coal blocks in Orissa’s Talabira II & III in 2005.\n" +
                "\n" +
                "The FIR against Mr. Birla, Mr. Parakh and others was registered in October 2013 by the CBI which had alleged that Mr. Parakh had reversed his decision to reject coal block allocation to Birla’s firm Hindalco within months “without any valid basis or change in circumstances” and shown “undue favours”.\n" +
                "\n" +
                "The CBI had booked Mr. Birla, Mr. Parakh and other Hindalco officials under various IPC sections, including criminal conspiracy and criminal misconduct on part of government officials.\n" +
                "\n" +
                "Earlier on November 10, the CBI had told the court that there was “prima facie enough material” to proceed against some private parties and public servants in the case.\n" +
                "\n" +
                "The Supreme Court-appointed special public prosecutor (SPP) R.S. Cheema for the CBI had submitted before the judge that the court can take cognisance of the offences mentioned in the closure report as there was prima facie “evidence against the accused to show their involvement”.\n" +
                "\n" +
                "Keywords: Coalgate, coal blocks allocation scam, coal scam, Kumar Mangalam Birla, Hindalco, P.C. Parakh, Talabira coal blocks, Manmohan Singh";

        String sample1 = "what is meant by Kaposi's sarcoma, once a rarely seen neoplasm in the West, now occurs in an epidemic fashion in association with acquired immune deficiency syndrome (AIDS).\n" +
                "The pathogenesis of Kaposi's sarcoma is still unclear but it appears to be an endothelial neoplasm.\n" +
                "Its clinical presentation may be quite subtle and varied.\n" +
                "The natural history of Kaposi's sarcoma is still not fully defined, and its rate of progression may be either relatively indolent or aggressive.\n" +
                "Therapies include local radiation, recombinant interferon alfa-2a, and cytotoxic chemotherapy.\n" +
                "For a subset of patients with Kaposi's sarcoma who were treated with recombinant interferon alfa-2a, the disease is in complete remission, without opportunistic infection, and they appear to be culture-negative for the etiologic retrovirus that causes their immune deficiency.\n" +
                "Interferon alfa-2a appears to have antineoplastic efficacy, (and may have antiretroviral efficacy as well) in this epidemic neoplasm.";

        String sample2 = "Investigation into the Saradha chit fund scam has so far not revealed any terror link with Bangladesh, the Centre said today, days after the BJP had alleged such a connection.\n" +
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

        String sample3 = "Teresa h meng founded Atheros communications. Meng served on the board of Atheros. Charles barratt was the CEO of Atheros inc. Barratt and Meng were directors of Atheros.";

        sample2 =  sample2.replace("\n","");
        TAConfig config = new TAConfig();
        config.analysisConfig.put("Entity","TRUE");
        //config.analysisConfig.put("LSEntity","TRUE");
        config.analysisConfig.put("Category","TRUE");
        config.analysisConfig.put("Sentiment","TRUE");

        config.entityConfig.put("Person","TRUE");
        config.entityConfig.put("Organization","TRUE");
        config.entityConfig.put("Location","TRUE");
        config.entityConfig.put("Date","TRUE");
        config.entityConfig.put("Time","TRUE");
        config.entityConfig.put("Currency","TRUE");
        config.entityConfig.put("Percent","TRUE");

        TextManager temp = new TextManager();
        System.out.println(temp.tagUniqueTextAnalyticsComponentsINJSON(sample3,config));
    }

}
