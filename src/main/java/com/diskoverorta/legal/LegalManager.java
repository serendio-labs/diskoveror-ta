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
    static CorefManager m_coref = new CorefManager();
    static StanfordNLP m_snlp = new StanfordNLP();
    static EntityManager m_eManager = new EntityManager();
//    static OntologyManager m_oManager = new OntologyManager();
    static
    {
        setConfigInformation();
    }
    public LegalManager()
    {

    }
    public String tagLegalTextAnalyticsComponents(String sDoc)
    {
        Map<String,Map<String,Set<String>>> coref_out = m_coref.getCorefForSelectedEntites(sDoc,null,null,m_config.corefConfig);
        List<String> sentList = m_snlp.splitSentencesINDocument(sDoc);
        String jsonOutput = "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        Map<String,String> gpersonCoref = getCorefInvMap(coref_out.get("Person"));
        Map<String,String> gorgCoref = getCorefInvMap(coref_out.get("Organization"));

        Map<String,String> personCoref = null;
        Map<String,String> orgCoref = null;
        Map<String,Set<String>> ontologyTemp = null;

        List<LegalObject> legalcomponents = new ArrayList<>();
        for(String temp : sentList)
        {
            LegalObject legalcomponent = new LegalObject();
//            ontologyTemp = m_oManager.getOntologyForSelectedTerms(temp,m_config.ontologyConfig);

            legalcomponent.sentenceText = temp;
            legalcomponent.entities = m_eManager.getSelectedEntitiesForSentence(temp,m_config.entityConfig);
            legalcomponent.personAlias = getMatchedCoref(gpersonCoref, legalcomponent.entities.person);
            legalcomponent.orgAlias = getMatchedCoref(gorgCoref,legalcomponent.entities.organization);
            legalcomponent.events = ontologyTemp.get("Events");
            legalcomponent.topics = ontologyTemp.get("Topics");

            legalcomponents.add(legalcomponent);
        }
        jsonOutput = gson.toJson(legalcomponents);
        return jsonOutput;
    }
    public String tagLegalTextAnalyticsComponents(String sDoc, Map<String,String> apiConfig)
    {

        Set<String> personEntities = new HashSet<>();
        Set<String> orgEntities = new HashSet<>();

        List<String> sentList = m_snlp.splitSentencesINDocument(sDoc);
        String chunkSize = null;

        if((apiConfig!=null)&&(apiConfig.containsKey("chunksize")==true))
            chunkSize = apiConfig.get("chunksize");

        if(chunkSize != null)
            sentList = chunkSentences(sentList,chunkSize);

        String jsonOutput = "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Map<String,Set<String>> ontologyTemp = null;

        List<LegalObject> legalcomponents = new ArrayList<>();
        for(String temp : sentList)
        {
            LegalObject legalcomponent = new LegalObject();
//            ontologyTemp = m_oManager.getOntologyForSelectedTerms(temp,m_config.ontologyConfig);
            legalcomponent.sentenceText = temp;
            legalcomponent.entities = m_eManager.getSelectedEntitiesForSentence(temp,m_config.entityConfig);
            insertEntity(personEntities,legalcomponent.entities.person);
            insertEntity(orgEntities,legalcomponent.entities.organization);
//            legalcomponent.events = ontologyTemp.get("Events");
//            legalcomponent.topics = ontologyTemp.get("Topics");

            legalcomponents.add(legalcomponent);
        }
        Map<String,Map<String,Set<String>>> coref_out = m_coref.getCorefForSelectedEntites(sDoc,personEntities,orgEntities,m_config.corefConfig);
        Map<String,String> gpersonCoref = getCorefInvMap(coref_out.get("Person"));
        Map<String,String> gorgCoref = getCorefInvMap(coref_out.get("Organization"));

        for(LegalObject temp : legalcomponents)
        {
            temp.personAlias = getMatchedCoref(gpersonCoref, temp.entities.person);
            temp.orgAlias = getMatchedCoref(gorgCoref,temp.entities.organization);
        }

        jsonOutput = gson.toJson(legalcomponents);
        return jsonOutput;
    }
    void insertEntity(Set<String> gcoref,List<String> entity)
    {
        for(String temp : entity)
            gcoref.add(temp.toLowerCase().trim());
    }

    List<String> chunkSentences(List<String> sent, String count)
    {
        Integer size = Integer.parseInt(count);
        int total_size = sent.size();
        List<String> chunkedList = new ArrayList<String>();
        for(int i=0; i < sent.size();i = i + size)
        {
            String temp = "";
            for(int j=i;(j < i+size)&&(j<total_size);j++)
            {
                temp = temp+sent.get(j)+ " ";
            }
            chunkedList.add(temp.trim());
        }
        return chunkedList;
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
            content = Files.toString(new File("Novastar_ORIGINAL_TXT.txt_trimmed.txt"), Charsets.UTF_8);
            content = content.replace("\n"," ");
            content = content.replace("\r"," ");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        Map<String,String> config1 = new TreeMap<String,String>();
        config1.put("chunksize","5");
        System.out.println(lobj.tagLegalTextAnalyticsComponents(content,config1));
    }
}
