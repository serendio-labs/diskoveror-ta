package com.diskoverorta.tamanager;

import com.diskoverorta.entities.EntityManager;
import com.diskoverorta.osdep.StanfordNLP;
import com.diskoverorta.utils.EntityUtils;
import com.diskoverorta.vo.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by praveen on 11/11/14.
 */
public class TextManager
{
    static StanfordNLP nlpStanford = null;
    DocumentObject doc = null;
    public TextManager()
    {
        if(nlpStanford==null)
        {
            nlpStanford = new StanfordNLP();
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

    public static void main(String args[])
    {
        String sample = " Sri Lanka may consider releasing the Indian boats in its custody soon after verifying details of ownership, BJP leader Subramanian Swamy said here on Wednesday.\n" +
                "\n" +
                "“I have given a list of the boat numbers and their owners to the Defence Secretary, explaining that many of them belong to poor fishermen and not rich trawler-owners,” he said, after a meeting with Gotabaya Rajapaksa, Defence Secretary, and brother of President Mahinda Rajapaksa.\n" +
                "\n" +
                "“They will verify if the names listed are of the actual owners. If that is true, they will be inclined to release the boats,” Dr. Swamy said.";
        TAConfig config = new TAConfig();
        config.analysisConfig.put("Entity","TRUE");
        config.entityConfig.put("Person","TRUE");
        config.entityConfig.put("Organization","TRUE");
        config.entityConfig.put("Location","TRUE");
        TextManager temp = new TextManager();
        System.out.println(temp.tagTextAnalyticsComponentsINJSON(sample,config));
    }

}
