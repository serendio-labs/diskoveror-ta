package com.diskoverorta.legal;

import com.diskoverorta.coreference.CorefManager;
import com.diskoverorta.entities.EntityManager;
import com.diskoverorta.ontology.OntologyManager;
import com.diskoverorta.osdep.StanfordNLP;
import com.diskoverorta.vo.APIOutput;
import com.diskoverorta.vo.EntityObject;
import com.diskoverorta.vo.LegalObject;
import com.diskoverorta.vo.TAConfig;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by praveen on 4/2/15.
 */
public class LegalManager
{
    static TAConfig m_config = new TAConfig();
    static
    {
        setConfigInformation();
    }
    public String tagLegalTextAnalyticsComponents(String sDoc)
    {
        Map<String,Map<String,Set<String>>> coref_out = (new CorefManager().getCorefForSelectedEntites(sDoc,m_config.corefConfig));
        List<String> sentList = (new StanfordNLP().splitSentencesINDocument(sDoc));
        String jsonOutput = "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        EntityManager eManager = new EntityManager();
        OntologyManager oManager = new OntologyManager();
        Map<String,String> gpersonCoref = getCorefInvMap(coref_out.get("Person"));
        Map<String,String> gorgCoref = getCorefInvMap(coref_out.get("Organization"));

        Map<String,String> personCoref = null;
        Map<String,String> orgCoref = null;
        Map<String,Set<String>> ontologyTemp = null;

        List<LegalObject> legalcomponents = new ArrayList<>();
        for(String temp : sentList)
        {
            LegalObject legalcomponent = new LegalObject();
            ontologyTemp = oManager.getOntologyForSelectedTerms(temp,m_config.ontologyConfig);

            legalcomponent.sentenceText = temp;
            legalcomponent.entities = eManager.getSelectedEntitiesForSentence(temp,m_config.entityConfig);
            legalcomponent.personAlias = getMatchedCoref(gpersonCoref,legalcomponent.entities.person);
            legalcomponent.orgAlias = getMatchedCoref(gorgCoref,legalcomponent.entities.organization);
            legalcomponent.events = ontologyTemp.get("Events");
            legalcomponent.topics = ontologyTemp.get("Topics");

            legalcomponents.add(legalcomponent);
        }
        jsonOutput = gson.toJson(legalcomponents);
        return jsonOutput;
    }
    Map<String,String> getMatchedCoref(Map<String,String> globalCoref, List<String> entities)
    {
        Map<String,String> localCoref = new TreeMap<String,String>();
        for(String temp : entities)
        {
            if(globalCoref.containsKey(temp.toLowerCase().trim()) == true)
            {
                localCoref.put(temp,globalCoref.get(temp.toLowerCase().trim()));
            }
        }
        return localCoref;
    }
    Map<String,String> getCorefInvMap(Map<String,Set<String>> corefMap)
    {
        Map<String,String> corefinvMap = new TreeMap<>();
        for(String temp : corefMap.keySet())
        {
            Set<String> aliasSet = corefMap.get(temp);
            for(String temp1 : aliasSet)
            {
                if(corefinvMap.containsKey(temp1) == true)
                {
                    String keyTemp = corefinvMap.get(temp1);
                    if(keyTemp.length() < temp.length())
                        corefinvMap.put(temp1,temp);
                }
                else
                {
                    corefinvMap.put(temp1,temp);
                }

            }
        }
        return corefinvMap;
    }
    static void setConfigInformation()
    {
        m_config.analysisConfig.put("Entity","TRUE");
        m_config.analysisConfig.put("Coref","TRUE");

        m_config.corefConfig.put("Person","TRUE");
        m_config.corefConfig.put("Organization", "TRUE");
        m_config.corefConfig.put("CorefMethod","SUBSTRING");

        m_config.entityConfig.put("Person","TRUE");
        m_config.entityConfig.put("Organization","TRUE");
        m_config.entityConfig.put("Location","TRUE");
        m_config.entityConfig.put("Date","TRUE");

        m_config.ontologyConfig.put("Events","TRUE");
        m_config.ontologyConfig.put("Topics","TRUE");
    }
    public static void main(String args[])
    {
        LegalManager lobj = new LegalManager();
        String content = "";
        try {
            content = Files.toString(new File("tmp_kreiger1.txtssplit.txt.trimmed.txt"), Charsets.UTF_8);
            content = content.replace("\n"," ");
            content = content.replace("\r"," ");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        System.out.println(lobj.tagLegalTextAnalyticsComponents(content));
    }
}
