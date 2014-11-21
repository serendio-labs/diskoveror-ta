package com.diskoverorta.tamanager;

import com.diskoverorta.entities.EntityManager;
import com.diskoverorta.osdep.StanfordNLP;
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

    public String tagTextAnalyticsComponentsINJSON(String sDoc,TAConfig config)
    {
        DocSentObject doc = tagTextAnalyticsComponents(sDoc,config);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(doc);
        return jsonOutput;
    }

    public static void main(String args[])
    {
        String sample = "Suspended Trinamool Congress MP and Saradha scam accused Kunal Ghosh on Friday allegedly tried to commit suicide by taking sleeping pills inside the Alipore Central Jail, West Bengal Correctional Home Services Minister H A Swafi said.";
        TAConfig config = new TAConfig();
        config.analysisConfig.put("Entity","TRUE");
        config.entityConfig.put("Person","TRUE");
        config.entityConfig.put("Organization","TRUE");
        config.entityConfig.put("Location","TRUE");
        TextManager temp = new TextManager();
        System.out.println(temp.tagTextAnalyticsComponentsINJSON(sample,config));
    }

}
